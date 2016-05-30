package com.snail.contacts;

import android.test.AndroidTestCase;

import java.util.List;

/**
 * Created by ping on 2016/5/30.
 */
public class ContactTest extends AndroidTestCase {

    public void testQuery(){
        ContactDao contactDao=new ContactDao(getContext());
        List<Person> persons = contactDao.findAllContacts();
        for(int i=0;i<persons.size();i++){
            System.out.println(persons.get(i));
        }
    }

}
