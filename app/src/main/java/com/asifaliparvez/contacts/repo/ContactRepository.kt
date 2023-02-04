package com.asifaliparvez.contacts.repo

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.CallLog
import android.provider.CallLog.Calls
import android.provider.ContactsContract
import android.telecom.Call
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.asifaliparvez.contacts.model.CallLogsModel
import com.asifaliparvez.contacts.model.ContactsModel
import java.util.*
import kotlin.collections.ArrayList

class ContactRepository(val context: Context) {


    // Get All Contacts
    fun getAllContacts(columns:Array<String>? = null, ):ArrayList<ContactsModel>{
        val contactsArray = ArrayList<ContactsModel>()
        val cursor = context.contentResolver!!.query(
            ContactsContract.Contacts.CONTENT_URI,columns,null,null,
            ContactsContract.Contacts.DISPLAY_NAME
        )

        cursor?.apply {

            val nameColumns = getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
            val idColumns = getColumnIndex(ContactsContract.Contacts._ID)
            val lookUpKeyColumns = getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY)
            val photoColumns = getColumnIndex(ContactsContract.Contacts.PHOTO_URI)
            while (moveToNext()){
                val name = getString(nameColumns).trim()
                val id = getString(idColumns)
                val lookUpKey = getString(lookUpKeyColumns).trim()
                val photo:String = getString(photoColumns)?:"null"
                contactsArray.add(ContactsModel(name, id, photo, lookUpKey))


            }
        }
        cursor?.close()
        return  contactsArray
    }

    // Insert New Contact Using Intent
    fun addContact(number:String, name:String){
        val intent = Intent(ContactsContract.Intents.Insert.ACTION).apply {
            type = ContactsContract.RawContacts.CONTENT_TYPE
            putExtra(ContactsContract.Intents.Insert.PHONE, number)
            putExtra(ContactsContract.Intents.Insert.NAME, name)
        }
        context.startActivity(intent)
    }
    // Delete Contact Using Lookup Key. Return Number Of Rows Deleted
    fun deleteContact(lookUpKey: String):Int{
        return  context.contentResolver.delete(Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI, lookUpKey),null, null)
    }
    //TODO:: Implement This Update Feature
    fun updateContact(){

    }
    // Get All Numbers Associated With Name Using LookUpKey
    fun contactDetail(lookUpKey:String, name:String):ArrayList<String>{
        val arrayList = arrayListOf<String>()
        val array = arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER)
        val cursor = context.contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, array, "${ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY} LIKE '%$lookUpKey%' AND ${ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME} LIKE '%$name%'" , null, null)
        if (cursor != null) {
            val numberColumns = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                while (cursor.moveToNext()){
                    cursor.apply {
                        val number = getString(numberColumns).replace(" ","")
                        if (!arrayList.contains(number)){
                            arrayList.add(number)
                        }



                    }
                }

        }
        cursor?.close()
        return arrayList


    }

     @RequiresApi(Build.VERSION_CODES.O)
     fun getCallLogs():ArrayList<CallLogsModel>{

        val array = arrayListOf<CallLogsModel>()

        val cursor = context.contentResolver.query(CallLog.Calls.CONTENT_URI, null, null, null, "date DESC")
        Calls.DEFAULT_SORT_ORDER
        cursor?.apply {
            val nameIndex = getColumnIndex(Calls.CACHED_NAME)
            val idIndex = getColumnIndex(Calls._ID)
            val durIndex = getColumnIndex(Calls.DURATION)
            val numIndex = getColumnIndex(Calls.NUMBER)
            val dateIndex = getColumnIndex(Calls.DATE)
            val typeIndex = getColumnIndex(Calls.TYPE)
            val savedIndex = getColumnIndex(Calls.IS_READ)


            while (moveToNext()){
                val id = getString(idIndex)
                val name = getString(nameIndex)
                val duration = getString(durIndex)
                val number = getString(numIndex)
                val date = getString(dateIndex)
                val type = getString(typeIndex)


                array.add(CallLogsModel(id, name, number, duration, date, type))
            }
            cursor.close()
        }
        return array

    }

    fun getCallLogsOfSpecificNumber(number:String):ArrayList<CallLogsModel>{
        val array = arrayListOf<CallLogsModel>()
        val columns: Array<String> = arrayOf(
            Calls.NUMBER,
            Calls._ID,
            Calls.CACHED_NAME,
            Calls.CACHED_PHOTO_URI,
            Calls.TYPE,
            Calls.DURATION,
            Calls.IS_READ
        )
        val cursor = context.contentResolver.query(Calls.CONTENT_URI, null, "${Calls.NUMBER} LIKE $number", null, "date DESC")
        cursor?.apply {
            val nameIndex = getColumnIndex(Calls.CACHED_NAME)
            val idIndex = getColumnIndex(Calls._ID)
            val durIndex = getColumnIndex(Calls.DURATION)
            val numIndex = getColumnIndex(Calls.NUMBER)
        //    val dateIndex = getColumnIndex(Calls.DATE)
            val typeIndex = getColumnIndex(Calls.TYPE)
            val savedIndex = getColumnIndex(Calls.IS_READ)


            while (moveToNext()){
                val id = getString(idIndex)
                val name = getString(nameIndex)
                val duration = getString(durIndex)
                val number = getString(numIndex)
//                val date = getString(dateIndex)
                val type = getString(typeIndex)


                array.add(CallLogsModel(id, name, number, duration, "date", type))
            }
            cursor.close()
        }
        return array


    }

}