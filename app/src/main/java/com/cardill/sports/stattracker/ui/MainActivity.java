package com.cardill.sports.stattracker.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.cardill.sports.stattracker.AuthService;
import com.cardill.sports.stattracker.R;
import com.cardill.sports.stattracker.user.AuthRequestBody;
import com.cardill.sports.stattracker.network.CardillService;
import com.cardill.sports.stattracker.user.Session;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements HasSupportFragmentInjector {

    private static final int RC_SIGN_IN = 10;
    private static final String TAG = MainActivity.class.getName();

    @Inject
    DispatchingAndroidInjector<Fragment> fragmentInjector;

    @Inject
    CardillService mCardillService;

    @Inject
    AuthService authService;

    @Inject
    Session session;

    private FirebaseAuth mAuth;

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentInjector;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navView = findViewById(R.id.bottomNavigationView);

        navView.setOnNavigationItemSelectedListener(item -> {
            NavController navController = Navigation.findNavController(
                    this,
                    R.id.my_nav_host_fragment);

            switch (item.getItemId()) {
                case R.id.nav_game:
                    navController.navigate(R.id.newGameFragment);
                    return true;
                case R.id.nav_scores:
                    navController.navigate(R.id.scoresFragment);
                    return true;
                case R.id.nav_stats:
                    navController.navigate(R.id.statsFragment);
                    return true;
                default:
                    return false;
            }
        });

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                updateUI(user);
                // ...
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_log_out) {
            signOut();
            return true;
        } else if (item.getItemId() == R.id.action_delete_account) {
            deleteAccount();
            return true;
        } else if (item.getItemId() == R.id.action_test_recovery) {
            gotoRecoveryScreen();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void gotoRecoveryScreen() {
        Navigation.findNavController(
                this,
                R.id.my_nav_host_fragment).navigate(R.id.roomActivity);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return Navigation.findNavController(this, R.id.scoresFragment).navigateUp();
    }

    private void signOut() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(task -> updateUI(null));
    }

    private void deleteAccount() {
        AuthUI.getInstance()
                .delete(this)
                .addOnCompleteListener(task -> updateUI(null));
    }

    private void updateUI(FirebaseUser user) {
        if (user == null) {
            // Choose authentication providers
            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.EmailBuilder().build(),
                    new AuthUI.IdpConfig.GoogleBuilder().build());

            // Create and launch sign-in intent
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .setLogo(R.drawable.stat_logo)
                            .build(),
                    RC_SIGN_IN);
        } else {
            Timber.tag(TAG).d(user.getEmail());
            user.getIdToken(true)
                    .addOnCompleteListener(task -> {
                                String token = task.getResult().getToken();
                                authService.authenticate(new AuthRequestBody(token))
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(response -> session.saveToken(response.getId_token()),
                                                throwable -> Timber.tag("VITHUSHAN").e(throwable));
                            }

                    );

        }
    }
}
