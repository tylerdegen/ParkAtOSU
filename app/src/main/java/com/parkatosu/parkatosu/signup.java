package com.parkatosu.parkatosu;



import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class signup extends Fragment implements OnClickListener {
    private EditText username;
    private EditText password;
    private EditText confirm;
    private DatabaseHelper dh;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_signup, container, false);
        username= (EditText)v.findViewById(R.id.username);
        password= (EditText)v.findViewById(R.id.password);
        confirm= (EditText)v.findViewById(R.id.password_confirm);
        View btnAdd= (Button)v.findViewById(R.id.done_button);
        btnAdd.setOnClickListener(this);
        View btnCancel= (Button)v.findViewById(R.id.cancel_button);
        btnCancel.setOnClickListener(this);
        return v;
    }

    private void createAccount(){
        String user = username.getText().toString();
        String pass = password.getText().toString();
        String conf = confirm.getText().toString();
        if ((pass.equals(conf))&&(!user.equals(""))&&(!password.equals(""))){
            this.dh = new DatabaseHelper(this.getActivity());
            this.dh.insertAccount(user, pass);
            Toast.makeText(this.getActivity(),"New record inserted",Toast.LENGTH_SHORT).show();
        }
        else if(!pass.equals(conf)){
            new AlertDialog.Builder(this.getActivity()).setTitle("Error").setMessage("Passwords do not match")
                    .setNeutralButton("Try Again", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.done_button:
                createAccount();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                break;
            case R.id.cancel_button:
                username.setText("");
                password.setText("");
                confirm.setText("");
                break;
        }
    }
}
