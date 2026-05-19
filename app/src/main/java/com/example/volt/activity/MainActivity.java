package com.example.volt.activity;

import static com.robotemi.sdk.map.OnLoadMapStatusChangedListener.COMPLETE;

import android.animation.LayoutTransition;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Switch;
import android.content.pm.PackageManager;
import androidx.core.content.ContextCompat;
import androidx.core.app.ActivityCompat;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.ernestoyaquello.dragdropswiperecyclerview.DragDropSwipeRecyclerView;
import com.example.volt.audio.AudioManager;
import com.example.volt.R;
import com.example.volt.commnad.Command;
import com.example.volt.commnad.GoToCommand;
import com.example.volt.currentposition.CurrentPositionManager;
import com.example.volt.exit.ExitManager;
import com.example.volt.map.MapManager;
import com.example.volt.map.MapView;
import com.example.volt.popup.PopupManager;
import com.example.volt.popup.PopupView;
import com.example.volt.screen.FAQView;
import com.example.volt.screen.ScreenManager;
import com.example.volt.selected.DestinationCollection;
import com.example.volt.selected.DestinationReorderableQueue;
import com.example.volt.selected.SelectedDestinationsManager;
import com.example.volt.search.DestinationsSearchManager;
import com.example.volt.specialdestinations.AerialDistanceAlgorithm;
import com.example.volt.specialdestinations.SpecialDestinationsManager;
import com.example.volt.status.StatusListener;
import com.example.volt.status.StatusManager;
import com.example.volt.voice.VoiceManager;
import com.robotemi.sdk.Robot;
import com.robotemi.sdk.listeners.OnRobotReadyListener;
import java.util.ArrayList;
import com.robotemi.sdk.TtsRequest;


public class MainActivity extends AppCompatActivity implements OnRobotReadyListener{

    private SelectedDestinationsManager     selectedDestinationsManager;
    private AudioManager                    audioManager;
    private PopupManager                    popupManager;
    private MapManager                      mapManager;
    private DestinationsSearchManager       destinationsSearchManager;
    private SpecialDestinationsManager      specialDestinationsManager;
    private CurrentPositionManager          currentPositionManager;
    private StatusManager                   statusManager;
    private StatusListener                  statusListener;
    private ExitManager                     exitManager;
    private ScreenManager                   screenManager;
    private Command<String>                 goToCommand;
    private VoiceManager                    voiceManager;
    private static final int REQ_REC_AUDIO = 1001;
    private boolean                         isInitialized       = false;
    private boolean                         listenersAttached   = false;

    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState A bundle containing the previous state of the activity, if available.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Sets the main layout of the activity */
        setContentView(R.layout.activity_main);

        /* Hides the system navigation bar */
        hideSystemBars();

        Log.d("burger", "onCreate: ");
        Robot.getInstance().addOnRobotReadyListener(this);
    }

    /**
     * Called when the activity becomes visible to the user.
     * This method is invoked after onCreate().
     */
    @Override
    protected void onStart() {
        super.onStart();
        //this.ensureTemiSelectedAppOrPrompt();
    }

    /**
     * Called when the activity is resumed and is ready for user interaction.
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (isInitialized && !listenersAttached) {
            this.addRobotListeners();
            listenersAttached = true;
        }
    }

    /**
     * Called when the activity is partially visible (e.g., when another activity appears in front of it).
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (this.audioManager != null) {
            this.audioManager.pausePlayback();
        }
        if (listenersAttached) {
            this.removeRobotListeners();
            listenersAttached = false;
        }
    }

    /**
     * Called when the activity is no longer visible to the user.
     */
    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (this.audioManager != null) {
            this.audioManager.clearMediaPlayer();
        }
        isInitialized = false;
        listenersAttached = false;
        super.onDestroy();
    }


    @Override
    public void onRobotReady(boolean isReady) {
        if (isReady && !isInitialized) {
            this.initialize();
            this.addRobotListeners();
            listenersAttached = true;
            isInitialized = true;
        }
    }

    private void ensureTemiSelectedAppOrPrompt() {
        try {
            // 1) Try to detect if we’re already selected (method name varies by SDK)
            Boolean isSelected = null;
            try {
                isSelected = (Boolean) Robot.class
                        .getMethod("isSelectedApp")
                        .invoke(Robot.getInstance());
            } catch (NoSuchMethodException ignore) {
                // older/newer SDK name:
                try {
                    isSelected = (Boolean) Robot.class
                            .getMethod("isSelectedKioskApp")
                            .invoke(Robot.getInstance());
                } catch (NoSuchMethodException ignore2) { /* fall through */ }
            }
            if (Boolean.TRUE.equals(isSelected)) {
                return; // already Selected/Kiosk app
            }

            // 2) Try to open temi’s "Selected App" chooser (user must confirm)
            try {
                Robot.class.getMethod("requestToBeKioskApp")
                        .invoke(Robot.getInstance());
                // If this exists, temi will pop its chooser UI now.
                return;
            } catch (NoSuchMethodException ignore) {
                // some SDKs use a different name:
                try {
                    Robot.class.getMethod("requestToBeSelectedApp")
                            .invoke(Robot.getInstance());
                    return;
                } catch (NoSuchMethodException ignore2) {
                    // no programmatic entry on this firmware — show manual instructions
                }
            }
        } catch (Exception e) {
            // reflection or invoke failed; continue to manual path
        }

        // 3) Manual instructions (works on all builds)
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Enable voice control")
                .setMessage(
                        "To let this app fully control speech, set it as the Selected App:\n\n" +
                                "Settings → Apps → Selected app → choose this app → Confirm.\n\n" +
                                "Also turn off the built-in assistant (if present):\n" +
                                "Settings → Speech / Voice Assistant → disable replies / wake word."
                )
                .setPositiveButton("Open Settings", (d, w) -> {
                    try {
                        // Generic Android Settings as a fallback. (User navigates from here.)
                        startActivity(new android.content.Intent(android.provider.Settings.ACTION_SETTINGS));
                    } catch (Exception ignored) {}
                })
                .setNegativeButton("Close", null)
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ_REC_AUDIO) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Robot.getInstance().finishConversation();
                new Handler(Looper.getMainLooper()).postDelayed(
                        () -> Robot.getInstance().askQuestion("please say the name of the place you want to go"),
                        150
                );
            } else {
                Toast.makeText(this, "Mic permission is required.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Hides the system navigation bar and enables it to reappear on swipe.
     */
    private void hideSystemBars() {
        WindowInsetsControllerCompat controller = new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView());
        controller.hide(WindowInsetsCompat.Type.systemBars());
        controller.setSystemBarsBehavior(WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
    }

    /**
     * Initializes all application components, linking them to their respective UI elements.
     */
    private void initialize() {
        /* Retrieves UI elements that are needed in more than one class */
        Button                      goButton                 = findViewById(R.id.GoButton);
        ImageView                   voiceImageView           = findViewById(R.id.VoiceImageView);
        DragDropSwipeRecyclerView   destinationsRecyclerView = findViewById(R.id.DestinationsRecyclerView);
        PopupView                   leftPopupView            = findViewById(R.id.DestinationPopupViewLeft);
        PopupView                   rightPopupView           = findViewById(R.id.DestinationPopupViewRight);
        MapView                     mapView                  = findViewById(R.id.MapView);
        SearchView                  destinationsSearchView   = findViewById(R.id.DestinationsSearchView);
        ListView                    destinationListView      = findViewById(R.id.DestinationsListView);
        DestinationCollection       selectedDestinations     = new DestinationReorderableQueue();
        ImageView                   nearestMenImageView      = findViewById(R.id.NearestMenToiletImageView);
        ImageView                   nearestWomenImageView    = findViewById(R.id.NearestWomenToiletImageView);
        ImageView                   nearestElevatorImageView = findViewById(R.id.NearestElevatorImageView);
        ImageView                   nearestShelterImageView  = findViewById(R.id.NearestShelterImageView);
        Switch                      audioFeedbackSwitch      = findViewById(R.id.AudioFeedbackSwitch);
        ImageView                   currentPositionImageView = findViewById(R.id.currentLocationImageView);
        RecyclerView                statusRecyclerView       = findViewById(R.id.WarningsRecyclerView);
        ImageView                   exitImageView            = findViewById(R.id.ExitImageView);
        FAQView                     faqView                  = findViewById(R.id.SimpleFAQView);
        ImageView                   opneFaqImageView         = findViewById(R.id.InfoImageView);

        this.popupManager = new PopupManager(
            leftPopupView,
            rightPopupView,
            selectedDestinations
        );

        this.mapManager = new MapManager(
            mapView,
            this.popupManager
        );

        this.goToCommand = new GoToCommand(this.mapManager.getDestinationToViewMap().keySet());
        this.selectedDestinationsManager = new SelectedDestinationsManager(
            destinationsRecyclerView,
            selectedDestinations,
            goButton,
            this.goToCommand
        );

        this.voiceManager = new VoiceManager(
                this,
                this,
                voiceImageView,
                this.goToCommand
        );

        this.popupManager.addOnPopupViewClosedListener(this.mapManager);
        this.popupManager.addOnPopupDestinationChangedListener(this.mapManager);

        this.destinationsSearchManager = new DestinationsSearchManager(
            this,
            destinationsSearchView,
            destinationListView,
            new ArrayList<>(this.mapManager.getDestinationToViewMap().keySet()),
            this.mapManager,
            this.popupManager
        );

        this.specialDestinationsManager = new SpecialDestinationsManager(
            nearestMenImageView,
            nearestWomenImageView,
            nearestElevatorImageView,
            nearestShelterImageView,
            this.mapManager.getDestinationToViewMap().values(),
            currentPositionImageView,
            new AerialDistanceAlgorithm()
        );

        this.audioManager = new AudioManager(
            audioFeedbackSwitch
        );

        this.currentPositionManager = new CurrentPositionManager(
            currentPositionImageView
        );

        this.statusManager = new StatusManager(
            statusRecyclerView
        );

        this.statusListener = new StatusListener(
            this.statusManager
        );

        this.exitManager = new ExitManager(
            exitImageView
        );

        this.screenManager = new ScreenManager(
            faqView,
            opneFaqImageView
        );

        selectedDestinations.addListener(this.selectedDestinationsManager);

        /* Enables layout transitions */
        LayoutTransition layoutTransition = new LayoutTransition();
        ((ConstraintLayout)findViewById(R.id.RootConstraintLayout)).setLayoutTransition(layoutTransition);
    }

    /**
     * Handles touch events in the activity.
     * This method is triggered when the user touches the screen. It determines
     * whether the touch event occurred inside the search view and, if not, removes
     * focus from the search view.
     *
     * @param event The MotionEvent containing details about the touch event.
     * @return True if the event was handled, otherwise the default event handling.
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            /* If the touch is outside the search view, removes focus */
            if (!this.destinationsSearchManager.isPointInsideSearch(event.getRawX(), event.getRawY())) {
                this.destinationsSearchManager.removeFocus();
            }
        }

        /* Passes the event to other views */
        return super.dispatchTouchEvent(event);
    }

    public PopupManager getPopupManager() { return this.popupManager; }

    private void addRobotListeners() {
        Robot.getInstance().addOnGoToLocationStatusChangedListener(this.audioManager);
        Robot.getInstance().addOnGoToLocationStatusChangedListener(this.selectedDestinationsManager);
        Robot.getInstance().addOnCurrentPositionChangedListener(this.currentPositionManager);
        Robot.getInstance().addOnRobotLiftedListener(this.statusListener);
        Robot.getInstance().addOnRobotDragStateChangedListener(this.statusListener);
        Robot.getInstance().addOnReposeStatusChangedListener(this.statusListener);
        Robot.getInstance().addOnBatteryStatusChangedListener(this.statusListener);
        Robot.getInstance().addAsrListener(this.voiceManager);
        Robot.getInstance().addTtsListener(this.voiceManager);
    }

    private void removeRobotListeners() {
        Robot.getInstance().removeOnGoToLocationStatusChangedListener(this.audioManager);
        Robot.getInstance().removeOnGoToLocationStatusChangedListener(this.selectedDestinationsManager);
        Robot.getInstance().removeOnCurrentPositionChangedListener(this.currentPositionManager);
        Robot.getInstance().removeOnRobotLiftedListener(this.statusListener);
        Robot.getInstance().removeOnRobotDragStateChangedListener(this.statusListener);
        Robot.getInstance().removeOnReposeStatusChangedListener(this.statusListener);
        Robot.getInstance().removeOnBatteryStatusChangedListener(this.statusListener);
        Robot.getInstance().removeAsrListener(this.voiceManager);
        Robot.getInstance().removeTtsListener(this.voiceManager);
    }
}