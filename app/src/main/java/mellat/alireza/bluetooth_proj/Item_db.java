package mellat.alireza.bluetooth_proj;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class Item_db extends SQLiteOpenHelper{
    static String databaseName="item_data";
    static int databaseVersion=1;
    static String tableName="items";
    static String fieldId="_id";
    static String fieldName="name";
    static Context context1;
    static String fielddate="date";
    public Item_db(Context context) {

        super(context, databaseName, null, databaseVersion);;
        context1=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CreateTableQuery="CREATE TABLE "+tableName + " ( "
                + fieldId + " integer primary key autoincrement , "
                + fieldName + " text , "
                + fielddate +" text )";
        db.execSQL(CreateTableQuery);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists "+tableName );
        onCreate(db);
    }







    //to insert sth into the database
    public void insertvoid(String name,String date){
        try{
            SQLiteDatabase db=this.getWritableDatabase();
            ContentValues cv=new ContentValues();
            cv.put(fieldName,name);
            cv.put(fielddate,date);
            db.insert(tableName,null,cv);
            db.close();}
        catch(Exception a){
            Toast.makeText(context1, "oops! an error has just occurred,please try again", Toast.LENGTH_LONG).show();


        }
    }



    public Cursor selectAllvoid(){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.rawQuery("select * from "+tableName ,null);
        return c;
    }
    public  Cursor selectOneVoid(String NAME){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.rawQuery("select * from items where name= '"+ NAME+"'",null);
        return c;
    }



    public  Cursor selectidvoid(int id){
        SQLiteDatabase db=this.getReadableDatabase();
        //Cursor c=db.rawQuery("select * from items where _id= "+ id,null);
        Cursor c=db.rawQuery("select * from "+tableName+ " where "+fieldId+" = " + String.valueOf(id),null);
        return c;
    }














    public void updateVoid (String name,String date,String id){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor c=db.rawQuery("update "+tableName+ " set "+fieldName+" = '"+name+"' , "+fielddate+" = '"+ date+"' where "+fieldId+" = "+id,null);

    }







    //set the amount of each food to zero
    public void resetvoid(){
        String query="delete from "+tableName ;
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL(query);
    }
}
