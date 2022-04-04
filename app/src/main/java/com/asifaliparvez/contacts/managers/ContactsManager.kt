package com.asifaliparvez.contacts.managers

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.CallLog
import android.provider.ContactsContract
import com.asifaliparvez.contacts.model.CallLogsModel
import com.asifaliparvez.contacts.model.ContactsModel

class ContactsManager(val context: Context) {


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
    //TODO : Add Call Features
    private fun getCallLogs(){

    }

}