package com.cs411.netflix;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cs411.netflix.GsonTemplates.SimpleResponse;

import java.util.ArrayList;

public class PrimaryLoginActivity extends AppCompatActivity{

    EditText userEntry, passEntry;
    Button createAcctBtn;
    final boolean PASSWORD_IMPLEMENTED = false; //set to true once passwords have been impl.ed
    //preferences key/values
    final String saveCredKey = "saveCredentials";
    final String usernameKey = "username";
    final String passwordKey = "password";
    final int doSaveCredentials = 1; //save
    final int doNotSaveCredentials = 0; //not now
    final int neverSaveCredentials = 3; //never
    final int defSaveCredentials = 2; //default
    final String defUsername = "DEFAULTUSERNAME";
    final String defPassword = "DEFAULTPASSWORD";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primary_login);
        userEntry = (EditText) findViewById(R.id.usernameEntryPL);
        passEntry = (EditText) findViewById(R.id.passwordEntryPL);

        //set the underline color to white
        ColorStateList csl = ColorStateList.valueOf(Color.WHITE);
        ViewCompat.setBackgroundTintList(userEntry, csl);
        ViewCompat.setBackgroundTintList(passEntry, csl);

        //shared preferences code
        SharedPreferences settings = PrimaryLoginActivity.this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        int saveCredentials = settings.getInt(saveCredKey, defSaveCredentials);
        //this is not the first launch or the user decided not to save credentials
        if(saveCredentials == doSaveCredentials){
            //check that username and password are not default before populating
            String savedUser = settings.getString(usernameKey, defUsername);
            String savedPass = settings.getString(passwordKey, defPassword);
            if(!savedUser.equals(defUsername)){
                userEntry.setText(savedUser);
                ColorStateList cslgreen = ColorStateList.valueOf(Color.GREEN);
                ViewCompat.setBackgroundTintList(userEntry, cslgreen);
            }
            else{
                Log.d("PLA", "creds saved but username default");
            }
            if(!savedPass.equals(defPassword)){
                passEntry.setText(savedPass);
                ColorStateList cslgreen = ColorStateList.valueOf(Color.GREEN);
                ViewCompat.setBackgroundTintList(passEntry, cslgreen);
            }
        }

        createAcctBtn = (Button) findViewById(R.id.createAccountButtonPL);
        createAcctBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(PrimaryLoginActivity.this, LoginActivity.class));
            }
        });
    }

    /**
     *  This method handles the onclick for the login button
     *  It will query the database for the username/password combo defined in the edit text views above
     *  It will call NetflixAsyncTask
     * */
    public void verifyCredentials(View v){
        String sUser = userEntry.getText().toString();
        String sPass = passEntry.getText().toString();
        if(TextUtils.isEmpty(sUser)){
            userEntry.setError("Username cannot be empty");
            return;
        }
        else if(PASSWORD_IMPLEMENTED && TextUtils.isEmpty(sPass)){
            passEntry.setError("Password cannot by empty");
            return;
        }

        hideKeyboard(this);

        NetflixAsyncTask asyncTask = new NetflixAsyncTask();
        asyncTask.primaryLoginActivity = this;

        ArrayList<String> params = new ArrayList<>();
        params.add("GET");
        params.add("search_users(");
        params.add(sUser);
        String[] paramsArr = new String[params.size()];
        paramsArr = params.toArray(paramsArr);
        Log.d("PLActivity:verifyCred.s", "searching for username");
        asyncTask.execute(paramsArr);
    }

    /**
     *  This method handles the response from NetflixAsyncTask
     *  The activity switches here
     *  Error checks, such as user not found, done here
     *  Credential saving checks/dialog done here
     * */
    public void handleResponse(SimpleResponse response){
        if(response.getStatusCode() == 200){
            //found username (no password checking)
            //set the underline color to green
            ColorStateList csl = ColorStateList.valueOf(Color.GREEN);
            ViewCompat.setBackgroundTintList(userEntry, csl);

            final String sUser = userEntry.getText().toString();
            final String sPass = passEntry.getText().toString();

            //saved preferences dialog
            SharedPreferences settings = PrimaryLoginActivity.this.getPreferences(Context.MODE_PRIVATE);
            final SharedPreferences.Editor editor = settings.edit();
            int saveCredentials = settings.getInt(saveCredKey, defSaveCredentials);
            System.out.println("REACHED 1");

            //do not prompt if save is set to never or set to save (because already saved)
            if(true){//saveCredentials != neverSaveCredentials && saveCredentials != doSaveCredentials) {
                System.out.println("REACHED 2");

                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("Save Credentials?")
                        .setMessage("Would you like to save your login credentials for faster future login?")
                        .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                editor.putInt(saveCredKey, doSaveCredentials);
                                editor.putString(usernameKey, sUser);
                                editor.putString(passwordKey, sPass);
                                editor.commit(); //synchronous as opposed to the asynchronous apply()
                                System.out.println("REACHED 3");
                                //start next activity only after dialog has been addressed
                                //TODO: update the activity we switch to from SearchContentActivity to Dashboard activity
                                //Intent intent = new Intent(PrimaryLoginActivity.this, SearchContentActivity.class);
                                Intent intent = new Intent(PrimaryLoginActivity.this, DashboardActivity.class);
                                intent.putExtra("username", sUser); //the second param here is kind of insecure, should really just save in a global at end of verifyCredentials
                                startActivity(intent);
                            }
                        })
                        .setNeutralButton("Not Now", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                editor.putInt(saveCredKey, doNotSaveCredentials);
                                editor.commit();

                                //start next activity only after dialog has been addressed
                                //TODO: update the activity we switch to from SearchContentActivity to Dashboard activity
                                //Intent intent = new Intent(PrimaryLoginActivity.this, SearchContentActivity.class);
                                Intent intent = new Intent(PrimaryLoginActivity.this, DashboardActivity.class);
                                intent.putExtra("username", sUser); //the second param here is kind of insecure, should really just save in a global at end of verifyCredentials
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("Never Save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                editor.putInt(saveCredKey, neverSaveCredentials);
                                editor.commit();

                                //start next activity only after dialog has been addressed
                                //TODO: update the activity we switch to from SearchContentActivity to Dashboard activity
                                //Intent intent = new Intent(PrimaryLoginActivity.this, SearchContentActivity.class);
                                Intent intent = new Intent(PrimaryLoginActivity.this, DashboardActivity.class);
                                intent.putExtra("username", sUser); //the second param here is kind of insecure, should really just save in a global at end of verifyCredentials
                                startActivity(intent);
                            }
                        })
                        .create();
                dialog.show();

            }
            else{
                //don't edit preferences, only open dashboard
                Intent intent = new Intent(PrimaryLoginActivity.this, DashboardActivity.class);
                intent.putExtra("username", sUser);
                startActivity(intent);
            }

        }
        else{
            if(response.getStatusCode() == 409){
                //username was not found
                //make a toast (dialog seems too intrusive)
                Toast.makeText(this, "Username not found", Toast.LENGTH_LONG).show();
                //set the underline color to red
                ColorStateList csl = ColorStateList.valueOf(Color.RED);
                ViewCompat.setBackgroundTintList(userEntry, csl);
                return; //do nothing and do not switch activities, let the user retype the username
            }
            else{
                //this really shouldn't happen as there're only 2 codes being returned by the php
                Log.e("PLA:handleResponse", "response code not 409 and username not found");
                return; //do nothing; idk how we even got here
            }
        }
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
