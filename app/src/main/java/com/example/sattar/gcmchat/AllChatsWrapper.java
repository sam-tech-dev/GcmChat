package com.example.sattar.gcmchat;

/**
 * Created by Sattar on 6/5/2016.
 */
public class AllChatsWrapper {
        String  name, number,date,time,content,noofSms;
        Integer id;

        public AllChatsWrapper() {
            // TODO Auto-generated constructor stub
        }

        public	void set_id(Integer idofsms){

            this.id=idofsms;

        }

        public	void set_name(String nameofprsn){

            this.name=nameofprsn;

        }


        public	void set_number(String mono){

            this.number=mono;

        }

        public	void set_date(String dat){

            this.date=dat;

        }


        public	void set_time(String timeofsms){

            this.time=timeofsms;

        }

        public	void set_content(String description){

            this.content=description;

        }

        public	void set_count(String noSms){

            this.noofSms=noSms;

        }





        public	Integer get_id(){

            return this.id;

        }

        public	String get_name(){

            return this.name;

        }

        public	String get_number(){

            return this.number;

        }




        public	String get_date(){

            return this.date;

        }

        public	String get_time(){

            return this.time;

        }

        public	String get_content(){

            return this.content;
        }

        public	String get_count(){

            return this.noofSms;
        }


}


