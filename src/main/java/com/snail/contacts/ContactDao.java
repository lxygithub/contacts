package com.snail.contacts;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ping on 2016/5/30.
 */
public class ContactDao {

    private Context mContext;

    public ContactDao(Context context) {
        mContext = context;
    }

    public List<Person> findAllContacts() {
        List<Person> resuls = null;
        Person person = null;
        //获取所有的联系人
        Cursor contact = this.mContext.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, new String[]{ContactsContract.Contacts._ID}, null, null, null);
        if (contact != null) {
            resuls = new ArrayList<>();
            while (contact.moveToNext()) {
                int contact_ID = contact.getInt(0);
                //通过联系人的id获取元数据id
                Cursor rawContacts = this.mContext.getContentResolver().query(ContactsContract.RawContacts.CONTENT_URI, new String[]{ContactsContract.RawContacts._ID}, ContactsContract.RawContacts.CONTACT_ID + "=?", new String[]{contact_ID + ""}, null);
                if (rawContacts != null) {
                    while (rawContacts.moveToNext()) {
                        int rawContacts_Id = rawContacts.getInt(0);
                        //通过元数据id查询数据--->真实的联系人信息
                        Cursor datas = this.mContext.getContentResolver().query(ContactsContract.Data.CONTENT_URI, null, ContactsContract.Data.RAW_CONTACT_ID + "=?", new String[]{rawContacts_Id + ""}, null);
                        if (datas != null) {
                            person = new Person();
                            person.setId(contact_ID);
                            //设置数据
                            while (datas.moveToNext()) {
                                String mimeType = datas.getString(datas.getColumnIndex(ContactsContract.Data.MIMETYPE));
                                String actureData = datas.getString(datas.getColumnIndex(ContactsContract.Data.DATA1));
                                if (mimeType.equals("vnd.android.cursor.item/phone_v2")) {
                                    person.setPhone(actureData);
                                }else if(mimeType.equals("vnd.android.cursor.item/postal-address_v2")){
                                    person.setAddres(actureData);
                                }else if(mimeType.equals("vnd.android.cursor.item/email_v2")){
                                    person.setEmail(actureData);
                                }else if(mimeType.equals("vnd.android.cursor.item/name")){
                                    person.setName(actureData);
                                }
                            }
                            resuls.add(person);
                            person = null;
                            datas.close();
                        }
                    }
                    rawContacts.close();
                }
            }
            contact.close();
        }
        return resuls;
    }

}
