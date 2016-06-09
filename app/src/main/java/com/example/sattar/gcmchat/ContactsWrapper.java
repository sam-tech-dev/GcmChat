package com.example.sattar.gcmchat;

/**
 * Created by Sattar on 6/7/2016.
 */
public class ContactsWrapper {

        String  name, phoneNumbers,status;


        public ContactsWrapper() {
            // TODO Auto-generated constructor stub
        }



        public	void set_name(String name){

            this.name=name;

        }



    public	void set_status(String stat){

        this.status=stat;

    }



    public	void set_numbers(String mono){

            this.phoneNumbers=mono;

        }


        public	String get_name(){

            return this.name;

        }

    public	String get_status(){

        return this.status;

    }

        public	String get_numbers(){

            return this.phoneNumbers;

        }


    }

