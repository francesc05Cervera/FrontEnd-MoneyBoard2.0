package com.moneyboard.android;

import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.content.res.ColorStateList;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.content.Context;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private static final int BG=Color.rgb(14,20,36), SURFACE=Color.rgb(21,30,48),
            SURFACE_ALT=Color.rgb(32,43,64), PRIMARY=Color.rgb(136,117,255),
            GREEN=Color.rgb(41,197,150), BLUE=Color.rgb(57,143,203),
            AMBER=Color.rgb(209,139,45), RED=Color.rgb(214,55,80),
            TEXT=Color.rgb(243,245,252), MUTED=Color.rgb(155,168,194),
            BORDER=Color.rgb(44,57,82);
    private final ExecutorService executor=Executors.newSingleThreadExecutor();
    private ApiClient api;
    private SessionStore session;
    private LinearLayout root, content;
    private TextView pageTitle;
    private JsonArray sections=new JsonArray();
    private JsonObject selectedSection;
    private boolean showingTransactions=false;

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        getWindow().setStatusBarColor(BG);
        getWindow().setNavigationBarColor(BG);
        getWindow().getDecorView().setSystemUiVisibility(0);
        api=new ApiClient();
        try { session=new SessionStore(this); }
        catch(Exception e) { toast("Impossibile inizializzare la sessione sicura."); finish(); return; }
        if(session.token()!=null) loadSections(); else showLogin();
    }

    private void base(String title) {
        root=new LinearLayout(this); root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(BG); root.setPadding(dp(20),dp(14),dp(20),dp(14));
        setContentView(root);
        LinearLayout header=new LinearLayout(this); header.setGravity(Gravity.CENTER_VERTICAL);
        header.setOrientation(LinearLayout.HORIZONTAL);
        TextView brand=label("MONEYBOARD",18,TEXT,true); brand.setLetterSpacing(.08f);
        header.addView(brand,new LinearLayout.LayoutParams(0,-2,1));
        pageTitle=label(title,14,MUTED,false);
        header.addView(pageTitle);
        root.addView(header);
        View line=new View(this); line.setBackgroundColor(BORDER);
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(-1,dp(1)); lp.topMargin=dp(14); lp.bottomMargin=dp(16);
        root.addView(line,lp);
        ScrollView scroll=new ScrollView(this); scroll.setFillViewport(true);
        content=new LinearLayout(this); content.setOrientation(LinearLayout.VERTICAL);
        scroll.addView(content);
        root.addView(scroll,new LinearLayout.LayoutParams(-1,0,1));
    }

    private void showLogin() {
        base("IL TUO SPAZIO FINANZIARIO");
        addSpace(28);
        content.addView(label("Bentornato.",30,TEXT,true));
        content.addView(label("Gestisci i tuoi risparmi in modo semplice.",14,MUTED,false));
        addSpace(28);
        TextInputEditText email=field("Email",false), password=field("Password",true);
        content.addView(input("INDIRIZZO EMAIL",email));
        content.addView(input("PASSWORD",password));
        addSpace(14);
        MaterialButton login=button("Accedi",PRIMARY);
        content.addView(login,matchWrap(54));
        MaterialButton register=button("Crea un account",SURFACE_ALT);
        content.addView(register,matchWrap(52));
        login.setOnClickListener(v->{
            String e=value(email), p=value(password);
            if(e.isBlank()||p.isBlank()){toast("Inserisci email e password.");return;}
            login.setEnabled(false);
            runApi(()->{
                JsonObject body=new JsonObject();body.addProperty("email",e);body.addProperty("password",p);
                String response=api.request("POST","/user/login",body,null);
                String token=JsonParser.parseString(response).getAsJsonObject().get("token").getAsString();
                String[] parts=token.split("\\.");
                if(parts.length<2)throw new IllegalStateException("Token ricevuto non valido.");
                String payload=new String(android.util.Base64.decode(parts[1],android.util.Base64.URL_SAFE|android.util.Base64.NO_WRAP|android.util.Base64.NO_PADDING),java.nio.charset.StandardCharsets.UTF_8);
                JsonObject claims=JsonParser.parseString(payload).getAsJsonObject();
                if(!claims.has("sub"))throw new IllegalStateException("Token senza identificativo utente.");
                session.save(token,e);
                runOnUiThread(this::loadSections);
            },()->login.setEnabled(true));
        });
        register.setOnClickListener(v->showRegister());
    }

    private void showRegister() {
        base("NUOVO ACCOUNT");
        addSpace(22);
        content.addView(label("Inizia con MoneyBoard",27,TEXT,true));
        content.addView(label("Crea il tuo spazio personale.",14,MUTED,false));
        addSpace(24);
        TextInputEditText username=field("Nome utente",false), email=field("Email",false), password=field("Password",true);
        content.addView(input("NOME UTENTE",username)); content.addView(input("EMAIL",email)); content.addView(input("PASSWORD",password));
        MaterialButton create=button("Registrati",PRIMARY), back=button("Torna al login",SURFACE_ALT);
        content.addView(create,matchWrap(54)); content.addView(back,matchWrap(50));
        create.setOnClickListener(v->{
            String u=value(username),e=value(email),p=value(password);
            if(u.isBlank()||e.isBlank()||p.isBlank()){toast("Compila tutti i campi.");return;}
            JsonObject body=new JsonObject();body.addProperty("username",u);body.addProperty("email",e);body.addProperty("password",p);
            create.setEnabled(false);
            runApi(()->{api.request("POST","/user/register",body,null);runOnUiThread(()->{toast("Account creato. Ora puoi accedere.");showLogin();});},()->create.setEnabled(true));
        });
        back.setOnClickListener(v->showLogin());
    }

    private void loadSections() {
        runApi(()->{
            String json=api.request("GET","/sezione",null,session.token());
            sections=JsonParser.parseString(json).getAsJsonArray();
            runOnUiThread(()->showDashboard());
        },()->{});
    }

    private void showDashboard() {
        showingTransactions=false;selectedSection=null;
        base("PANORAMICA");
        LinearLayout welcome=card();
        welcome.addView(label("I tuoi risparmi, sotto controllo",22,TEXT,true));
        welcome.addView(label("Tutte le tue sezioni in un unico posto.",14,MUTED,false));
        content.addView(welcome);
        LinearLayout titleRow=new LinearLayout(this);titleRow.setGravity(Gravity.CENTER_VERTICAL);
        titleRow.addView(label("Le tue sezioni",21,TEXT,true),new LinearLayout.LayoutParams(0,-2,1));
        MaterialButton refresh=smallButton("Aggiorna",BLUE);titleRow.addView(refresh);
        content.addView(titleRow);
        refresh.setOnClickListener(v->loadSections());
        if(sections.size()==0){
            LinearLayout empty=card();empty.addView(label("Ancora nessuna sezione",18,TEXT,true));
            empty.addView(label("Crea il tuo primo obiettivo di risparmio.",14,MUTED,false));content.addView(empty);
        }
        for(int i=0;i<sections.size();i++){
            JsonObject section=sections.get(i).getAsJsonObject();
            LinearLayout item=card();
            LinearLayout top=new LinearLayout(this);top.setGravity(Gravity.CENTER_VERTICAL);
            TextView icon=label("€",22,PRIMARY,true);top.addView(icon,new LinearLayout.LayoutParams(0,-2,1));
            TextView goal=label("OBIETTIVO",11,MUTED,true);top.addView(goal);
            item.addView(top);
            addSpaceTo(item,12);
            item.addView(label(string(section,"nome","Sezione"),18,TEXT,true));
            addSpaceTo(item,5);
            item.addView(label("SALDO ATTUALE",10,MUTED,true));
            item.addView(label(money(number(section,"saldo")),25,GREEN,true));
            double target=number(section,"obbiettivo");
            if(target<=0)target=number(section,"obiettivo");
            if(target>0){
                item.addView(label("Obiettivo: "+money(target),12,MUTED,false));
                android.widget.ProgressBar progress=new android.widget.ProgressBar(this,null,android.R.attr.progressBarStyleHorizontal);
                progress.setMax(100);progress.setProgress((int)Math.min(100,Math.max(0,number(section,"saldo")/target*100)));
                progress.setProgressTintList(ColorStateList.valueOf(GREEN));
                progress.setProgressBackgroundTintList(ColorStateList.valueOf(SURFACE_ALT));
                LinearLayout.LayoutParams pp=new LinearLayout.LayoutParams(-1,dp(6));pp.topMargin=dp(8);item.addView(progress,pp);
            }
            LinearLayout actions=new LinearLayout(this);actions.setGravity(Gravity.CENTER_VERTICAL);
            MaterialButton open=smallButton("Movimenti",BLUE), delete=smallButton("Elimina",RED);
            actions.addView(open,new LinearLayout.LayoutParams(0,dp(42),1));
            LinearLayout.LayoutParams delp=new LinearLayout.LayoutParams(0,dp(42),1);delp.leftMargin=dp(8);actions.addView(delete,delp);
            addSpaceTo(item,10);item.addView(actions);content.addView(item);
            open.setOnClickListener(v->{selectedSection=section;loadTransactions();});
            delete.setOnClickListener(v->confirmDeleteSection(section));
        }
        MaterialButton add=button("＋  Nuova sezione",PRIMARY), logout=button("Esci",RED);
        content.addView(add,matchWrap(54));content.addView(logout,matchWrap(50));
        add.setOnClickListener(v->showCreateSection());logout.setOnClickListener(v->{session.clear();showLogin();});
    }

    private void showCreateSection() {
        base("NUOVO OBIETTIVO");addSpace(24);
        content.addView(label("Crea una sezione",27,TEXT,true));
        content.addView(label("Dai un nome al tuo obiettivo e imposta il traguardo.",14,MUTED,false));
        addSpace(24);
        TextInputEditText name=field("Nome sezione",false), target=field("Obiettivo (€)",false);
        content.addView(input("NOME",name));content.addView(input("OBIETTIVO DI RISPARMIO",target));
        MaterialButton save=button("Crea sezione",GREEN),cancel=button("Annulla",SURFACE_ALT);
        content.addView(save,matchWrap(54));content.addView(cancel,matchWrap(50));
        save.setOnClickListener(v->{
            String n=value(name), t=value(target).replace(',','.');
            if(n.isBlank()||t.isBlank()){toast("Compila tutti i campi.");return;}
            double amount;try{amount=Double.parseDouble(t);}catch(Exception e){toast("Inserisci un importo valido.");return;}
            if(amount<=0){toast("L'obiettivo deve essere maggiore di zero.");return;}
            JsonObject body=new JsonObject();body.addProperty("nome",n);body.addProperty("obbiettivo",amount);
            save.setEnabled(false);runApi(()->{api.request("POST","/sezione/create",body,session.token());runOnUiThread(this::loadSections);},()->save.setEnabled(true));
        });
        cancel.setOnClickListener(v->showDashboard());
    }

    private void confirmDeleteSection(JsonObject section) {
        new MaterialAlertDialogBuilder(this).setTitle("Elimina sezione")
                .setMessage("Vuoi eliminare \""+string(section,"nome","")+"\"?")
                .setNegativeButton("Annulla",null).setPositiveButton("Elimina",(d,w)->{
                    JsonObject body=new JsonObject();body.addProperty("nome",string(section,"nome",""));
                    runApi(()->{api.request("DELETE","/sezione",body,session.token());runOnUiThread(this::loadSections);},()->{});
                }).show();
    }

    private void loadTransactions() {
        if(selectedSection==null)return;
        runApi(()->{
            String id=string(selectedSection,"id","");
            String json=api.request("GET","/transazione/sezione/"+id,null,session.token());
            JsonArray transactions=JsonParser.parseString(json).getAsJsonArray();
            runOnUiThread(()->showTransactions(transactions));
        },()->{});
    }

    private void showTransactions(JsonArray transactions) {
        showingTransactions=true;base("MOVIMENTI");addSpace(8);
        content.addView(label(string(selectedSection,"nome","Sezione"),25,TEXT,true));
        content.addView(label("Cronologia delle entrate e delle uscite",14,MUTED,false));
        addSpace(12);
        if(transactions.size()==0){LinearLayout empty=card();empty.addView(label("Nessun movimento",17,TEXT,true));empty.addView(label("Aggiungi la prima transazione.",13,MUTED,false));content.addView(empty);}
        for(int i=0;i<transactions.size();i++){
            JsonObject tx=transactions.get(i).getAsJsonObject();
            boolean incoming=tx.has("isEntrata")?tx.get("isEntrata").getAsBoolean():tx.has("entrata")&&tx.get("entrata").getAsBoolean();
            LinearLayout item=card();LinearLayout row=new LinearLayout(this);row.setGravity(Gravity.CENTER_VERTICAL);
            LinearLayout info=new LinearLayout(this);info.setOrientation(LinearLayout.VERTICAL);
            info.addView(label(string(tx,"titolo","Movimento"),16,TEXT,true));
            info.addView(label(string(tx,"data",""),12,MUTED,false));
            row.addView(info,new LinearLayout.LayoutParams(0,-2,1));
            row.addView(label((incoming?"+ ":"− ")+money(number(tx,"importo")),17,incoming?GREEN:RED,true));
            item.addView(row);
            String description=string(tx,"descrizione","");
            if(!description.isBlank())item.addView(label(description,13,MUTED,false));
            MaterialButton delete=smallButton("Elimina movimento",RED);addSpaceTo(item,8);item.addView(delete);
            content.addView(item);
            delete.setOnClickListener(v->new MaterialAlertDialogBuilder(this).setTitle("Elimina movimento")
                    .setMessage("Vuoi eliminare questo movimento?").setNegativeButton("Annulla",null)
                    .setPositiveButton("Elimina",(d,w)->runApi(()->{
                        api.request("DELETE","/transazione/"+string(tx,"id",""),null,session.token());
                        runOnUiThread(this::loadTransactions);
                    },()->{})).show());
        }
        MaterialButton add=button("＋  Aggiungi movimento",GREEN),back=button("←  Torna alle sezioni",SURFACE_ALT);
        content.addView(add,matchWrap(54));content.addView(back,matchWrap(50));
        add.setOnClickListener(v->showAddTransaction());back.setOnClickListener(v->showDashboard());
    }

    private void showAddTransaction() {
        base("NUOVO MOVIMENTO");addSpace(20);
        content.addView(label("Aggiungi transazione",26,TEXT,true));
        content.addView(label("Sezione: "+string(selectedSection,"nome",""),14,MUTED,false));addSpace(18);
        TextInputEditText title=field("Titolo",false),description=field("Descrizione",false),amount=field("Importo (€)",false);
        content.addView(input("TITOLO",title));content.addView(input("DESCRIZIONE",description));content.addView(input("IMPORTO",amount));
        Switch incoming=new Switch(this);incoming.setText("È un'entrata");incoming.setTextColor(GREEN);incoming.setChecked(true);content.addView(incoming);
        MaterialButton save=button("Salva movimento",GREEN),cancel=button("Annulla",SURFACE_ALT);
        content.addView(save,matchWrap(54));content.addView(cancel,matchWrap(50));
        save.setOnClickListener(v->{
            String t=value(title),d=value(description),a= value(amount).replace(',','.');
            if(t.isBlank()||a.isBlank()){toast("Titolo e importo sono obbligatori.");return;}
            double value;try{value=Double.parseDouble(a);}catch(Exception e){toast("Inserisci un importo valido.");return;}
            if(value<=0){toast("L'importo deve essere maggiore di zero.");return;}
            JsonObject section=new JsonObject();section.addProperty("nome",string(selectedSection,"nome",""));
            JsonObject body=new JsonObject();body.addProperty("titolo",t);body.addProperty("descrizione",d);
            body.addProperty("data",java.time.LocalDate.now().toString());body.addProperty("importo",value);
            body.addProperty("entrata",incoming.isChecked());body.add("sezione",section);
            save.setEnabled(false);runApi(()->{api.request("POST","/transazione/create",body,session.token());runOnUiThread(this::loadTransactions);},()->save.setEnabled(true));
        });
        cancel.setOnClickListener(v->loadTransactions());
    }

    private void runApi(ThrowingRunnable work,Runnable onFailure) {
        executor.execute(()->{
            try{work.run();}
            catch(ApiClient.SessionExpiredException e){session.clear();runOnUiThread(()->{toast(e.getMessage());showLogin();});}
            catch(Exception e){runOnUiThread(()->{toast(e.getMessage()==null?"Si è verificato un errore.":e.getMessage());onFailure.run();});}
        });
    }

    private LinearLayout card() {
        MaterialCardView shell=new MaterialCardView(this);shell.setCardBackgroundColor(SURFACE);
        shell.setStrokeColor(BORDER);shell.setStrokeWidth(dp(1));shell.setRadius(dp(18));shell.setCardElevation(dp(1));
        LinearLayout inner=new LinearLayout(this);inner.setOrientation(LinearLayout.VERTICAL);
        inner.setPadding(dp(18),dp(18),dp(18),dp(18));shell.addView(inner);
        LinearLayout wrapper=new LinearLayout(this);wrapper.setOrientation(LinearLayout.VERTICAL);wrapper.addView(shell);
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(-1,-2);lp.bottomMargin=dp(14);wrapper.setLayoutParams(lp);
        return inner;
    }

    private TextInputLayout input(String hint,TextInputEditText edit) {
        TextInputLayout layout=new TextInputLayout(this);layout.setHint(hint);
        layout.setBoxBackgroundMode(TextInputLayout.BOX_BACKGROUND_OUTLINE);
        layout.setBoxStrokeColor(BORDER);layout.setHintTextColor(ColorStateList.valueOf(MUTED));
        layout.setBoxBackgroundColor(SURFACE_ALT);layout.setDefaultHintTextColor(ColorStateList.valueOf(MUTED));
        edit.setTextColor(TEXT);edit.setHintTextColor(MUTED);edit.setSingleLine(true);
        layout.addView(edit,new TextInputLayout.LayoutParams(-1,-2));
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(-1,-2);lp.bottomMargin=dp(15);layout.setLayoutParams(lp);
        return layout;
    }
    private TextInputEditText field(String hint,boolean secret) {
        TextInputEditText e=new TextInputEditText(this);e.setHint(hint);
        if(secret)e.setInputType(android.text.InputType.TYPE_CLASS_TEXT|android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
        else if(hint.toLowerCase(Locale.ROOT).contains("email"))e.setInputType(android.text.InputType.TYPE_CLASS_TEXT|android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        return e;
    }
    private MaterialButton button(String text,int color) {
        MaterialButton b=new MaterialButton(this);b.setText(text);b.setTextColor(Color.WHITE);
        b.setBackgroundTintList(ColorStateList.valueOf(color));b.setCornerRadius(dp(14));
        b.setAllCaps(false);b.setTextSize(15);b.setInsetTop(0);b.setInsetBottom(0);
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(-1,dp(52));lp.bottomMargin=dp(10);b.setLayoutParams(lp);return b;
    }
    private MaterialButton smallButton(String text,int color) {
        MaterialButton b=button(text,color);b.setTextSize(12);b.setMinHeight(dp(40));b.setMinimumHeight(dp(40));return b;
    }
    private TextView label(String text,int size,int color,boolean bold) {
        TextView v=new TextView(this);v.setText(text);v.setTextSize(size);v.setTextColor(color);
        if(bold)v.setTypeface(Typeface.DEFAULT,Typeface.BOLD);v.setGravity(Gravity.CENTER_VERTICAL);return v;
    }
    private void addSpace(int height){View v=new View(this);content.addView(v,new LinearLayout.LayoutParams(1,dp(height)));}
    private void addSpaceTo(LinearLayout panel,int height){View v=new View(this);panel.addView(v,new LinearLayout.LayoutParams(1,dp(height)));}
    private LinearLayout.LayoutParams matchWrap(int height){LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,dp(height));p.bottomMargin=dp(10);return p;}
    private String value(TextInputEditText e){return e.getText()==null?"":e.getText().toString().trim();}
    private String string(JsonObject o,String key,String fallback){return o.has(key)&&!o.get(key).isJsonNull()?o.get(key).getAsString():fallback;}
    private double number(JsonObject o,String key){try{return o.has(key)&&!o.get(key).isJsonNull()?o.get(key).getAsDouble():0;}catch(Exception e){return 0;}}
    private String money(double n){return NumberFormat.getCurrencyInstance(Locale.ITALY).format(n);}
    private int dp(int n){return (int)(n*getResources().getDisplayMetrics().density+.5f);}
    private void toast(String message){Toast.makeText(this,message,Toast.LENGTH_LONG).show();}
    @Override protected void onDestroy(){super.onDestroy();executor.shutdown();}
    private interface ThrowingRunnable{void run()throws Exception;}
}
