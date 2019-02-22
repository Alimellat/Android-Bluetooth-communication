package mellat.alireza.bluetooth_proj;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Edit extends AppCompatActivity {
    TextView idtext,nametext,datetext;
    EditText nameedit;
    Item_db db1=new Item_db(this);
    int ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        idtext = (TextView) findViewById(R.id.id_text);
        nametext = (TextView) findViewById(R.id.name_text);
        datetext = (TextView) findViewById(R.id.Date_text);
        nameedit = (EditText) findViewById(R.id.editname);
        Intent i2=getIntent();
        ID=i2.getIntExtra("id",0);
        Cursor cursor1=db1.selectidvoid(ID);
        cursor1.moveToFirst();
        idtext.setText(cursor1.getString(0));
        nametext.setText(cursor1.getString(1));
        datetext.setText(cursor1.getString(2));
    }
    public void edit_robo(View view){
        String a= nameedit.getText().toString();
        Cursor cursor1=db1.selectidvoid(ID);
        cursor1.moveToFirst();
        String newname,newdate;
        //newname=cursor1.getString(1);
        newdate=cursor1.getString(2);
        Toast.makeText(this, String.valueOf(ID)+" "+a+" "+newdate, Toast.LENGTH_SHORT).show();
        db1.updateVoid(a,newdate,String.valueOf(ID));








    }
}
