package it.polimi.sw.gianpaolocugola47.view;

import it.polimi.sw.gianpaolocugola47.messages.Message;

public interface handler {


    //questo metodo avvisa i client che i dati del server che hanno inserito non sono corretti
    void wrongData();


    //questo metodo gestisce tutti i possibili messaggi che un client può ricevere dal
    // server in base a quale fase di gioco si trova il client
    void update(Message m, boolean b);


    //questo metodo include tutti i messaggi che il server invia al client avvisandoli
    // che le informazioni date non sono corrette e che devono reinserirle
    void tryAgain(Message m);


    void go();

    void askServer();
}
