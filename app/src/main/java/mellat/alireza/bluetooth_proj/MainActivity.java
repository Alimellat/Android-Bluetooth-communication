package mellat.alireza.bluetooth_proj;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.concurrent.TimeUnit;
import android.content.Intent;
import android.app.Activity;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.text.SimpleDateFormat;
import java.util.Date;





public class MainActivity extends AppCompatActivity {

    //    private final String DEVICE_NAME="MyBTBee";
    boolean dataflag=true;
    String data_string="";
    String empty_string="";
   // data_string = "z";
    private final String DEVICE_ADDRESS="98:d3:31:fc:5e:ee";
    private final String DEVICE_ADDRESS2="98d3:31:fc5eee";
   Item_db db1=new Item_db(this);
   int iterator;




    private final UUID PORT_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");//Serial Port Service ID
    private BluetoothDevice device;
    private BluetoothSocket socket;
    private OutputStream outputStream;
    private InputStream inputStream;
    Button Receivebotton, startbotton,stopButton;
    TextView textView;
    ListView listView;

    boolean deviceConnected=false;
    Thread thread;
    byte buffer[];
    int bufferPosition;
    ArrayList<String> listitem;
    ArrayAdapter adapter;
    boolean stopThread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Receivebotton = (Button) findViewById(R.id.receive_button);
        startbotton = (Button) findViewById(R.id.start_button);
        listView=(ListView)findViewById(R.id.list1);


        iterator =0;




        stopButton = (Button) findViewById(R.id.stop_button);
        textView=(TextView)findViewById(R.id.text_view);

      //  db1.insertvoid("Ali","1 jan");
        listitem=new ArrayList<>();

        show_data_on_list();






    }
//
    private void show_data_on_list() {
        listitem.clear();
        Cursor cursor=db1.selectAllvoid();

        if(cursor.getCount() == 0){

           // Toast.makeText(this, "empty database", Toast.LENGTH_SHORT).show();
            adapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,listitem);
            listView.setAdapter(adapter);
        }
        else {


            while(cursor.moveToNext()){
                listitem.add(cursor.getString(1)+ "  "+cursor.getString(2));//name

               // Toast.makeText(this, cursor.getString(1), Toast.LENGTH_SHORT).show();

            }
            adapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,listitem);
            listView.setAdapter(adapter);
          //  Toast.makeText(this, String.valueOf(cursor.getCount()), Toast.LENGTH_SHORT).show();

        }

    }


    public boolean BTinit()
    {
        boolean found=false;
        BluetoothAdapter bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(),"Device doesn't Support Bluetooth",Toast.LENGTH_SHORT).show();
        }
        if(!bluetoothAdapter.isEnabled())
        {
            Intent enableAdapter = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableAdapter, 0);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();
        if(bondedDevices.isEmpty())
        {
            Toast.makeText(getApplicationContext(),"Please Pair the Device first",Toast.LENGTH_SHORT).show();
        }
        else
        {
            for (BluetoothDevice iterator : bondedDevices)
            {
               //if(iterator.getAddress().equals(DEVICE_ADDRESS) || iterator.getAddress().equals(DEVICE_ADDRESS2))
             //  {
                    device=iterator;
                    found=true;
                    break;
              //  }
            }
        }
        return found;
    }

    public boolean BTconnect()
    {
        boolean connected=true;
        try {
            socket = device.createRfcommSocketToServiceRecord(PORT_UUID);
            socket.connect();
        } catch (IOException e) {
            e.printStackTrace();
            connected=false;
        }
        if(connected)
        {
            try {
                outputStream=socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                inputStream=socket.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


        return connected;
    }



    public void Recieve_robo(View view) {
        Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();
       if(BTinit())
        {
            if(BTconnect())
            {

                deviceConnected=true;
                beginListenForData();

            }

        }

    }







    public void erase_robo(View view){
        db1.resetvoid();
        show_data_on_list();




    }

    public void Start_robo(View view){

        String string ="a";
        //string.concat("\n");
        try {
            outputStream.write(string.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //textView.append("\nSent Data:"+string+"\n");





    }



    public void stop_robo(View view){





        String string = "b";
        //string.concat("\n");
        try {
            outputStream.write(string.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //textView.append("\nSent Data:"+string+"\n");
    }


    void beginListenForData()
    {




        final Handler handler = new Handler();
        stopThread = false;
        buffer = new byte[1024];
        Thread thread  = new Thread(new Runnable()
        {
            public void run()
            {
                while(!Thread.currentThread().isInterrupted() && !stopThread)
                {
                    try
                    {


                        try {
                            TimeUnit.SECONDS.sleep(2);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }


                        // for(int i=0;i<10;i++) {
                            int byteCount = inputStream.available();
                            if (byteCount > 0) {
                                byte[] rawBytes = new byte[byteCount];
                                inputStream.read(rawBytes);
                                final String string = new String(rawBytes, "UTF-8");
                               //data_string+=string;
                                if(dataflag)
                                handler.post(new Runnable() {
                                    public void run() {


                                           // textView.append(string);
                                       // Toast.makeText(MainActivity.this, string, Toast.LENGTH_SHORT).show();
                                        //data_string=empty_string;
                                        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                                        Date date = new Date();
                                        if(((string.trim()).equals(String.valueOf(1168768266)))|| ((string.trim()).equals(String.valueOf(4565501)))){
                                            db1.insertvoid("blue",formatter.format(date));
                                          }
                                        else if(((string.trim()).equals(String.valueOf(1067383117))||((string.trim()).equals(String.valueOf(4169465))))){
                                            db1.insertvoid("white",formatter.format(date));
                                            }
                                        else if(((string.trim()).equals(String.valueOf(1565675310))) || ((string.trim()).equals(String.valueOf(6115919)))){
                                            db1.insertvoid("red",formatter.format(date));
                                            }
                                        else{
                                            db1.insertvoid(string,formatter.format(date));
                                            }
                                        iterator++;
                                        show_data_on_list();
                                    }
                                });


                            }
                        //}

                    }
                    catch (IOException ex)
                    {
                        stopThread = true;
                    }
                }
            }
        });

        thread.start();
    }







    @Override
    protected void onDestroy()  {
        super.onDestroy();
        stopThread=true;


        stopThread = true;
        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        deviceConnected=false;


    }







}




