package com.example.sporthallmanager; // מגדיר את שם החבילה של הקוד הזה באפליקציה

// יבוא של כל הספריות והמחלקות שנדרשות לפעולה של Login עם Firebase ו-Google
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Objects;

// מחלקת LoginActivity - אחראית למסך הכניסה של המשתמשים
public class LoginActivity extends AppCompatActivity {

    // אובייקט לטיפול באימות משתמשים של Firebase
    private FirebaseAuth auth;

    // אובייקט שמטפל בחיבור לחשבון Google
    private GoogleSignInClient googleSignInClient;

    // רכיבי ממשק להצגת תמונת פרופיל, שם ודוא"ל של המשתמש
    private ShapeableImageView imageView;
    private TextView name, mail;

    // משגר תוצאה של אקטיביטי - משמש לקבלת תוצאה של Google Sign-In
    private final ActivityResultLauncher<Intent> activityResultLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    // אם החזרה מהאקטיביטי של Google הצליחה
                    if (result.getResultCode() == RESULT_OK) {
                        // מנסה לקבל את החשבון שחתם דרך Google
                        // שאותה נתפוס בcatch וכך נשלח הודעה למשתמש ונטפל בבעיה בצורה בטוחה אם יש כשל בהתחברות ואם כן זורק שגיאה מסוג api exception
                        Task<GoogleSignInAccount> accountTask = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                        try {
                            GoogleSignInAccount signInAccount = accountTask.getResult(ApiException.class);

                            // שימוש בטוקן שקיבלנו כדי להתחבר ל-Firebase
                            AuthCredential authCredential = GoogleAuthProvider.getCredential(signInAccount.getIdToken(), null);

                            // התחברות ל-Firebase עם האישורים
                            auth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Log.e("XXX", "onComplete: " + task.isSuccessful()); // מדפיס האם ההתחברות הצליחה

                                    if (task.isSuccessful()) {
                                        // מקבל את המשתמש המחובר
                                        auth = FirebaseAuth.getInstance();

                                        // טוען את תמונת הפרופיל באמצעות Glide
                                        //Glide היא ספרייה שמאפשרת להוריד תמונה מהאינטרנט (או מקור אחר), לשמור אותה במטמון לצורך טעינה מהירה בעתיד, ולהציג אותה אוטומטית בתוך ImageView.
                                        Glide.with(LoginActivity.this)
                                                .load(Objects.requireNonNull(auth.getCurrentUser()).getPhotoUrl())
                                                .into(imageView);

                                        // מציג את שם המשתמש והאימייל
                                        name.setText(auth.getCurrentUser().getDisplayName());
                                        mail.setText(auth.getCurrentUser().getEmail());

                                        // הודעת הצלחה
                                        Toast.makeText(LoginActivity.this, "Signed in successfully!", Toast.LENGTH_SHORT).show();

                                        // מעבר למסך הראשי של האפליקציה עם שם המשתמש
                                        Intent intent = new Intent(LoginActivity.this, MainCalendarActivity.class);
                                        intent.putExtra("USERNAME", auth.getCurrentUser().getDisplayName());
                                        startActivity(intent);
                                    } else {
                                        // במקרה של שגיאה - מציג הודעת שגיאה
                                        Toast.makeText(LoginActivity.this, "Failed to sign in: " + task.getException(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } catch (ApiException e) {
                            e.printStackTrace(); // מדפיס את השגיאה אם נכשלה קבלת החשבון
                        }
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // קורא לפונקציית onCreate של המחלקה העליונה
        setContentView(R.layout.activity_main_login); // טוען את קובץ ה-XML של ממשק המשתמש

        // אתחול Firebase
        FirebaseApp.initializeApp(this);

        // קישור רכיבי ה-UI מתוך ה-XML לפי ה-ID
        imageView = findViewById(R.id.profileImage);
        name = findViewById(R.id.nameTV);
        mail = findViewById(R.id.mailTV);

        // הגדרת אפשרויות הכניסה עם חשבון Google - מבקש גם טוקן וגם אימייל
        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.client_id)) // מזהה הלקוח (client ID) מה-resources
                .requestEmail() // בקשת גישה לאימייל
                .build();

        // יצירת מופע של GoogleSignInClient עם האפשרויות שהוגדרו
        googleSignInClient = GoogleSignIn.getClient(LoginActivity.this, options);

        // אתחול Firebase Authentication
        auth = FirebaseAuth.getInstance();

        // מקשר את כפתור ההתחברות מה-XML
        SignInButton signInButton = findViewById(R.id.signIn);
        Log.e("XXX", "onCreate: " + auth.getCurrentUser()); // מדפיס את המשתמש הנוכחי אם קיים

        // מאזין ללחיצה על כפתור Google Sign-In
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("XXX", "onClick: "); // מדפיס שהכפתור נלחץ
                Intent intent = googleSignInClient.getSignInIntent(); // יוצר אינטנט לכניסה עם גוגל
                activityResultLauncher.launch(intent); // מפעיל את האינטנט לקבלת תוצאה
            }
        });
    }
}