package it.polimi.sw.gianpaolocugola47.messages;

import it.polimi.sw.gianpaolocugola47.utils.GameSerialized;

import java.io.Serializable;


    //classe astraatta che contiene tutte le caratteristiche che un messaggio dovrebbe avere
    public abstract class Message implements Serializable {

        private final MessageType messageType;
        private final String nickname;
        protected GameSerialized gameserialized;



        public Message(MessageType messageType, String nickname) {

            this.messageType = messageType;
            this.nickname = nickname;
        }



//        public Message(MessageType messageType, String nickname, Game model) {
//
//            this.gameserialized = new GameSerialized();
//            this.messageType = messageType;
//            this.nickname = nickname;
//        }



        public MessageType getMessageType() {
            return messageType;
        }



        public String getNickname() {
            return nickname;
        }
    }

