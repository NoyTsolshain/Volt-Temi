package com.example.volt.voice;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.example.volt.commnad.Command;
import com.robotemi.sdk.Robot;
import com.robotemi.sdk.TtsRequest;

/**
 * <h6>
 * {@link VoiceManager} orchestrates a two-step voice flow with temi:
 * (1) prompt user to say a destination; (2) ask for confirmation (yes/no).
 * It owns the conversation state machine and mediates between ASR callbacks and app commands.
 * </h6>
 *
 * <p><b>Representation Invariant:</b></p>
 * <ul>
 *   <li>{@code VoiceButton != null}</li>
 *   <li>{@code goToCommand != null}</li>
 * </ul>
 *
 * <p><b>Abstraction Function:</b></p>
 * A {@link VoiceManager} {@code vm} represents an interactive voice controller such that:
 * <ul>
 *   <li>{@code vm.VoiceButton} triggers the start of the voice interaction when clicked.</li>
 *   <li>{@code vm.asrState} governs the dialogue phase: awaiting a word or awaiting confirmation.</li>
 *   <li>{@code vm.pendingWord} holds the latest uttered destination to be confirmed/executed.</li>
 *   <li>{@code vm.goToCommand} is invoked with the confirmed destination.</li>
 * </ul>
 *
 * <p><b>Thread-safety:</b> Android callbacks are on the main thread; the few {@code volatile} fields
 * protect against reordering if future background interactions are introduced.</p>
 */

public class VoiceManager implements Robot.AsrListener, Robot.TtsListener{
    private final ImageView VoiceButton;
    private static final int REQ_REC_AUDIO = 1001;
    private enum AsrState { IDLE, AWAIT_WORD, AWAIT_CONFIRM }
    private volatile AsrState asrState = AsrState.IDLE;
    private volatile String pendingWord = "";

    // Track last TTS we started so we can close conversation exactly when it ends.
    private volatile com.robotemi.sdk.TtsRequest lastSpokenReq = null;
    private final Command<String> goToCommand;

    /**
     * <h5>Specification:</h5>
     * <ul>
     *   <li><b>requires:</b> {@code context != null}, {@code activity != null}, {@code voiceButton != null}, {@code goToCommand != null}</li>
     *   <li><b>modifies:</b> this</li>
     *   <li><b>effects:</b> Constructs a {@link VoiceManager}, wires the click listener</li>
     * </ul>
     */
    public VoiceManager(Context context, Activity activity, ImageView voiceButton, Command<String> goToCommand){
        this.VoiceButton = voiceButton;
        this.goToCommand = goToCommand;
        setupItemClickBehavior(context, activity);
        checkRepresentation();
    }

    /**
     * <h5>Specification:</h5>
     * <ul>
     *   <li><b>requires:</b> {@code context != null}, {@code activity != null}</li>
     *   <li><b>modifies:</b> this</li>
     *   <li><b>effects:</b> Binds the click listener to start the 2-step voice flow.
     *                       No automatic continuation after runtime permission grant.</li>
     * </ul>
     */
    private void setupItemClickBehavior(Context context, Activity activity) {
        this.VoiceButton.setOnClickListener(v -> {
            if (ensureMicPermission(context, activity)) {
                pendingWord = "";
                asrState = AsrState.AWAIT_WORD;
                Robot.getInstance().finishConversation();
                new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(
                        () -> Robot.getInstance().askQuestion("please say the name of the place you want to go"),
                        150
                );
            }
        });
    }

    /**
     * <h5>Specification:</h5>
     * <ul>
     *   <li><b>requires:</b> {@code context != null}, {@code activity != null}</li>
     *   <li><b>modifies:</b> none directly, may trigger Android permission dialog</li>
     *   <li><b>effects:</b> Returns {@code true} iff RECORD_AUDIO already granted;
     *                       otherwise requests it and returns {@code false}.</li>
     * </ul>
     */
    private boolean ensureMicPermission(Context context, Activity activity) {
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{android.Manifest.permission.RECORD_AUDIO}, REQ_REC_AUDIO);
            return false;
        }
        return true;
    }

    /**
     * <h5>Specification:</h5>
     * <ul>
     *   <li><b>requires:</b> temi ASR is active; this is invoked by the SDK.</li>
     *   <li><b>modifies:</b> {@code asrState}, {@code pendingWord}</li>
     *   <li><b>effects:</b> Implements the 2-step flow:
     *       <ol>
     *         <li>AWAIT_WORD → capture utterance & ask for yes/no confirmation.</li>
     *         <li>AWAIT_CONFIRM → speak result and (if yes) execute {@code goToCommand}.</li>
     *       </ol>
     *   </li>
     * </ul>
     */
    @Override
    public void onAsrResult(@NonNull String asrResult,
                            @NonNull com.robotemi.sdk.SttLanguage lang) {
        checkRepresentation();
        final String text = (asrResult == null) ? "" : asrResult.trim();
        if (text.isEmpty()) return;

        switch (asrState) {
            case AWAIT_WORD: {
                //pendingWord = text.split("\\s+")[0];
                pendingWord = text;

                Robot.getInstance().finishConversation();
                new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
                    asrState = AsrState.AWAIT_CONFIRM;
                    Robot.getInstance().askQuestion(
                            "Are you sure you want to go to " + pendingWord + "? Say yes or no."
                    );
                }, 150);
                break;
            }

            case AWAIT_CONFIRM: {
                final String lower = text.toLowerCase();
                final boolean yes = lower.startsWith("y") || lower.equals("yes")
                        || lower.equals("yeah") || lower.equals("yep") || lower.equals("ok")
                        || lower.equals("okay") || lower.equals("sure");
                final boolean no  = lower.startsWith("n") || lower.equals("no")
                        || lower.equals("nope") || lower.equals("nah") || lower.equals("cancel");

                Robot.getInstance().finishConversation();

                if (yes) {
                    boolean found= this.goToCommand.execute(pendingWord);
                    if(!found){
                        TtsRequest req1 = TtsRequest.create("Sorry, I couldn't find " + pendingWord + ". Please try another place.", /*showOnScreen*/ false);
                        lastSpokenReq = req1;
                        Robot.getInstance().speak(req1);
                    }
                    else{
                        TtsRequest req = TtsRequest.create("Let's go to " + pendingWord, /*showOnScreen*/ false);
                        lastSpokenReq = req;
                        Robot.getInstance().speak(req);
                    }
                } else if (no) {
                    TtsRequest req = TtsRequest.create("Canceled.", /*showOnScreen*/ false);
                    lastSpokenReq = req;
                    Robot.getInstance().speak(req);
                } else {
                    TtsRequest req = TtsRequest.create("Please say yes or no.", false);
                    lastSpokenReq = req;
                    Robot.getInstance().speak(req);
                }
                asrState = AsrState.IDLE;
                pendingWord = "";
                break;
            }

            default:
                break;
        }
        checkRepresentation();
    }

    /**
     * <h5>Specification:</h5>
     * <ul>
     *   <li><b>requires:</b> invoked by temi when TTS status changes.</li>
     *   <li><b>modifies:</b> none (except internal {@code lastSpokenReq})</li>
     *   <li><b>effects:</b> Closes the temi conversation exactly when our last TTS finishes,
     *                      preventing random assistant follow-ups and UI bubbles.</li>
     * </ul>
     */
    @Override
    public void onTtsStatusChanged(@NonNull TtsRequest ttsRequest) {
        checkRepresentation();
        if (ttsRequest == lastSpokenReq && ttsRequest.getStatus() == TtsRequest.Status.COMPLETED) {
            Robot.getInstance().finishConversation();
            lastSpokenReq = null;
        }
        checkRepresentation();
    }

    /**
     * <h5>Specification:</h5>
     * <ul>
     *   <li><b>requires:</b> none</li>
     *   <li><b>modifies:</b> none</li>
     *   <li><b>effects:</b> Asserts the representation invariant.</li>
     * </ul>
     *
     * @throws AssertionError if any invariant is violated.
     */
    private void checkRepresentation() {
        assert this.VoiceButton != null;
        assert this.goToCommand != null;
    }
}
