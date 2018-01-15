/**
 * 
 */
package funny.topic.free.jokes.db;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import funny.topic.free.jokes.db.entity.QOD;
import funny.topic.free.jokes.db.entity.Quote;
import funny.topic.free.jokes.utils.WriteLog;

/**
 * @author ThangTB
 *
 */
public class DataHeper {
	Context mContext;
	
//	public String databasePath = "data/data/funny.doctor.free.jokes/data.sqlite";
	public String databasePath = "";
	public String databaseName = "data.sqlite";
	public SQLiteDatabase database=null;	
	public File databaseFile;
	
	/**
	 * @param context
	 */
	public DataHeper(Context context) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
		databasePath = "data/data/"+context.getPackageName()+"/data.sqlite";
		databaseFile = new File(databasePath);
		if(!databaseFile.exists())
			try {
				deployDataBase(databaseName, databasePath);
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	/**
	 * 
	 * copy database to devices
	 * @param databaseName
	 * @param tagertPath
	 * @throws IOException
	 */
	private void deployDataBase(String databaseName, String tagertPath) throws IOException    {  
    	InputStream myInput = mContext.getAssets().open(databaseName);  
    	String outFileName = tagertPath;  	
    	OutputStream myOutput = new FileOutputStream(outFileName);  

    	byte[] buffer = new byte[1024]; 
    	int length; 
    	while ((length = myInput.read(buffer))>0)  
    	{  
    		myOutput.write(buffer, 0, length);   
    	}  
    	
    	myOutput.flush();  
    	myOutput.close();  
    	myInput.close();   
    }
	
	/**
	 * @param QuoteID
	 * @return
	 */
	public boolean AddFavourites(String QuoteID){
		String query="";
		query = "UPDATE quotes SET is_favourist = 1 WHERE  _id = "+QuoteID+";";
		
		try {
			database = SQLiteDatabase.openDatabase(databasePath, null, SQLiteDatabase.OPEN_READWRITE);
			database.execSQL(query);
			database.close();
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}
	
	/**
	 * @param QuoteID
	 * @return
	 */
	public boolean DeleteFavourites(String QuoteID){
		String query="";
		query = "UPDATE quotes SET is_favourist = 0 WHERE  _id = "+QuoteID+";";
		
		try {
			database = SQLiteDatabase.openDatabase(databasePath, null, SQLiteDatabase.OPEN_READWRITE);
			database.execSQL(query);
			database.close();
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}
	
	/**
	 * @param QuoteID
	 * @return
	 */
	public boolean DeleteAllFavourites(){
		String query="";
		query = "UPDATE quotes SET is_favourist = 0 WHERE  is_favourist = 1 ;";
		
		try {
			database = SQLiteDatabase.openDatabase(databasePath, null, SQLiteDatabase.OPEN_READWRITE);
			database.execSQL(query);
			database.close();
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}
	
	/**
	 * GET QUOTE COUNT
	 * @return
	 */
	public int getTotalQuotesNoFilter(){
		int i =0;
		String query="";
		Cursor  cursor;
		database = SQLiteDatabase.openDatabase(databasePath, null, SQLiteDatabase.OPEN_READWRITE);
    		query = "SELECT count(quotes._id) FROM quotes;";
    		WriteLog.d("ThangTB", "query: "+query);
    		try {
    			cursor =  database.rawQuery(query, null);	
        		if (cursor != null){
        	        boolean bool = cursor.moveToFirst();
        	        int j = cursor.getInt(0);
        	        i = j;
        	     }
        		
        		if (cursor!=null) {
    				cursor.close();
    			}
			} catch (Exception e) {
				// TODO: handle exception
			}finally{
				database.close();
			}
			
		return i;
		
	}
	
	/**
	 * get Quote list by limit
	 * @param start
	 * @param limit
	 * @param order
	 * @return
	 */
	public ArrayList<Quote> getQuoteByLimit(int start,int limit, String order){
		ArrayList<Quote> dataList = new ArrayList<Quote>();
		String query="";
		database = SQLiteDatabase.openDatabase(databasePath, null, SQLiteDatabase.OPEN_READWRITE);
		if (order!=null) {
			query = "select quotes._id,quotes.body,quotes.is_favourist FROM quotes " +
    				" GROUP BY quotes._id ORDER BY body "+order+ " Limit "+(start-1)+","+limit+";";
		}else{
			query = "select quotes._id,quotes.body,quotes.is_favourist FROM quotes " +
    				" GROUP BY quotes._id Limit "+(start-1)+","+limit+";";
		}
		WriteLog.d("ThangTB", "query: "+query);
        Cursor  cursor =  database.rawQuery(query, null);	 
        if (cursor==null || cursor.getCount()==0) {
       	 cursor.close();
            database.close();
       	return dataList;
		}
        cursor.moveToFirst();
        do
        {
        	Quote entity;
        	entity = new Quote(
        			Integer.parseInt(cursor.getString(0)),
        			cursor.getString(1),
        			Integer.parseInt(cursor.getString(2))
        			);
        	
        	dataList.add(entity);
        }while (cursor.moveToNext());
        cursor.close();
        database.close();
		return dataList;
	}
	
	
	/**
	 * get Quote list by fav
	 * @param authorid
	 * @return
	 */
	public ArrayList<Quote> getQuoteByFav(){
		ArrayList<Quote> dataList = new ArrayList<Quote>();
		String query="";
		database = SQLiteDatabase.openDatabase(databasePath, null, SQLiteDatabase.OPEN_READWRITE);
    		query = "select quotes._id,quotes.body,quotes.is_favourist from quotes WHERE is_favourist = 1 GROUP BY quotes._id Order by body asc;";
        Cursor  cursor =  database.rawQuery(query, null);	
        if (cursor==null || cursor.getCount()==0) {
        	 cursor.close();
             database.close();
        	return dataList;
		}
        cursor.moveToFirst();
        do
        {
        	Quote entity;
        	entity = new Quote(
        			Integer.parseInt(cursor.getString(0)),
        			cursor.getString(1),
        			Integer.parseInt(cursor.getString(2))
        			);
        	
        	dataList.add(entity);
        }while (cursor.moveToNext());
        cursor.close();
        database.close();
        WriteLog.d("ThangTB", "query: "+query);
		return dataList;
	}
	

	
	/**
	 * get Joke random
	 * @param total
	 * @return
	 */
	public Quote getQuoteRandom(int total){
		Quote entity = null;
		String query="";
		database = SQLiteDatabase.openDatabase(databasePath, null, SQLiteDatabase.OPEN_READWRITE);
		while (entity==null) {
			Random rand = new Random(); 
			int a =  rand.nextInt(total+1); 
			
			
			query = "select quotes._id,quotes.body,quotes.is_favourist " +
					" from quotes WHERE quotes._id ="+String.valueOf(a)+";";
			
			 Cursor  cursor =  database.rawQuery(query, null);	 
		        if (cursor==null || cursor.getCount()==0) {
		       	 cursor.close();
		            entity =null;
				}else{
					  cursor.moveToFirst();
				        do
				        {
				        	entity = new Quote(
				        			Integer.parseInt(cursor.getString(0)),
				        			cursor.getString(1),
				        			Integer.parseInt(cursor.getString(2))
				        			);
				        }while (cursor.moveToNext());
				        cursor.close();
				}
		}
		
		 database.close();
      WriteLog.d("ThangTB", "random quote = "+entity.getBody());
		return entity;
	}
	
	/**
	 * get Quote Next
	 * @param quoteId
	 * @return
	 */
	public Quote getNextQuote(int quoteId){
		Quote entity = null;
		String query="";
		database = SQLiteDatabase.openDatabase(databasePath, null, SQLiteDatabase.OPEN_READWRITE);
		query = "select quotes._id,quotes.body,quotes.is_favourist " +
				" from quotes WHERE quotes._id >"+quoteId+" LIMIT 1;";
		
		WriteLog.d("ThangTB", "next quote -->query= "+query);
		 Cursor  cursor =  database.rawQuery(query, null);	 
	        if (cursor==null || cursor.getCount()==0) {
	       	 cursor.close();
	            database.close();
	            entity =null;
			}else{
				  cursor.moveToFirst();
			        do
			        {
			        	entity = new Quote(
			        			Integer.parseInt(cursor.getString(0)),
			        			cursor.getString(1),
			        			Integer.parseInt(cursor.getString(2))
			        			);
			        }while (cursor.moveToNext());
			        cursor.close();
			        database.close();
			}
		
		return entity;
	}
	
	/**
	 * get Quote pre
	 * @param quoteId
	 * @return
	 */
	public Quote getPreQuote(int quoteId){
		Quote entity = null;
		String query="";
		int id;
		while (entity==null) {
			id = quoteId-1;
			database = SQLiteDatabase.openDatabase(databasePath, null, SQLiteDatabase.OPEN_READWRITE);
			query = "select quotes._id,quotes.body,quotes.is_favourist " +
					" from quotes WHERE quotes._id = "+id+";";
			
			WriteLog.d("ThangTB", "pre quote -->query= "+query);
			 Cursor  cursor =  database.rawQuery(query, null);	 
		        if (cursor==null || cursor.getCount()==0) {
		       	 cursor.close();
		            database.close();
		            entity =null;
				}else{
					  cursor.moveToFirst();
				        do
				        {
				        	entity = new Quote(
				        			Integer.parseInt(cursor.getString(0)),
				        			cursor.getString(1),
				        			Integer.parseInt(cursor.getString(2))
				        			);
				        }while (cursor.moveToNext());
				        cursor.close();
				        database.close();
				}
			}
		
		return entity;
	}
	
	/**
	 * get Quote pre
	 * @param quoteId
	 * @return
	 */
	public Quote getQuoteById(int quoteId){
		Quote entity = null;
		String query="";
			database = SQLiteDatabase.openDatabase(databasePath, null, SQLiteDatabase.OPEN_READWRITE);
			query = "select quotes._id,quotes.body,quotes.is_favourist " +
					" from quotes WHERE quotes._id = "+quoteId+";";
			
			WriteLog.d("ThangTB", "pre quote -->query= "+query);
			 Cursor  cursor =  database.rawQuery(query, null);	 
		        if (cursor==null || cursor.getCount()==0) {
		       	 cursor.close();
		            database.close();
		            entity =null;
				}else{
					  cursor.moveToFirst();
				        do
				        {
				        	entity = new Quote(
				        			Integer.parseInt(cursor.getString(0)),
				        			cursor.getString(1),
				        			Integer.parseInt(cursor.getString(2))
				        			);
				        }while (cursor.moveToNext());
				        cursor.close();
				        database.close();
				}
		
		return entity;
	}
	
	
	/**
	 * search Quote list
	 * @param s
	 * @return
	 */
	public ArrayList<Quote> getSearchQuoteByString(String s){
		ArrayList<Quote> dataList = new ArrayList<Quote>();
		String query="";
		database = SQLiteDatabase.openDatabase(databasePath, null, SQLiteDatabase.OPEN_READWRITE);
    		query = "select quotes._id,quotes.body,quotes.is_favourist " +
    				" from quotes " +
    				" WHERE quotes.body like  '%" + s+ "%'"+
    				" GROUP BY quotes._id " +
    				" Order by body asc;";
        Cursor  cursor =  database.rawQuery(query, null);	
        if (cursor==null || cursor.getCount()==0) {
        	 cursor.close();
             database.close();
        	return dataList;
		}
        cursor.moveToFirst();
        do
        {
        	Quote entity;
        	entity = new Quote(
        			Integer.parseInt(cursor.getString(0)),
        			cursor.getString(1),
        			Integer.parseInt(cursor.getString(2))
        			);
        	
        	dataList.add(entity);
        }while (cursor.moveToNext());
        cursor.close();
        database.close();
        WriteLog.d("ThangTB", "query: "+query);
		return dataList;
	}
	
	public boolean SaveQuoteOfDay(int quoteId, String body){
		Date d = new Date();
		String query="";
		query = "select * from qod LIMIT 1;";
		database = SQLiteDatabase.openDatabase(databasePath, null, SQLiteDatabase.OPEN_READWRITE);
		Cursor  cursor =  database.rawQuery(query, null);	 
        if (cursor==null || cursor.getCount()==0) {
        	query = "INSERT INTO qod (quote_id,changed,body) VALUES ("+quoteId+","+d.getTime()+",\""+body+"\") ;";
        	database.execSQL(query);
		}else {
			query = "UPDATE qod SET quote_id = "+quoteId+",changed = "+d.getTime()+",body = \"\""+body+"\"\" ;";
			StringBuilder stringBuilder = new StringBuilder();
			
			stringBuilder.append("UPDATE qod SET ")
			.append(" quote_id = ").append(quoteId).append(",changed = ").append(d.getTime())
			.append(",body = '").append(body.replaceAll("'", "''")).append("' ;");
			database.execSQL(stringBuilder.toString());
		}
		try {
			cursor.close();
			database.close();
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			cursor.close();
			database.close();
			return false;
		}finally{
			cursor.close();
			database.close();
		}
	}
	
	public QOD getQOD(){
		QOD entity;
		String query="";
		database = SQLiteDatabase.openDatabase(databasePath, null, SQLiteDatabase.OPEN_READWRITE);
		query = "select * from qod LIMIT 1;";
		
		 Cursor  cursor =  database.rawQuery(query, null);	 
	        if (cursor==null || cursor.getCount()==0) {
	       	 cursor.close();
	            database.close();
	            entity =null;
			}else{
				  cursor.moveToFirst();
			        do
			        {
			        	entity = new QOD(
			        			Integer.parseInt(cursor.getString(0)),
			        			Long.parseLong(cursor.getString(1)),
			        			cursor.getString(2)
			        			);
			        }while (cursor.moveToNext());
			        cursor.close();
			        database.close();
			}
		
		return entity;
	}
	
}
