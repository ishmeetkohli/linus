package com.example.harmeet.linus.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.example.harmeet.linus.R;
import com.example.harmeet.linus.utils.ExpandableListAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SeedSelectionActivity extends Activity {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, HashMap<String,String>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seed_selector);

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                String seedSongId = (String)listDataChild.get(listDataHeader.get(groupPosition)).keySet().toArray()[childPosition];
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("seedSongId",seedSongId);
                startActivity(intent);
                return false;
            }
        });
    }

    /*
     * Preparing the list data
     */
    private void prepareListData() {
        String seedList ="{\"Blues\":{\"SOXVLOJ12AB0189215\":\"Amor De Cabaret\",\"SOMJBYD12A6D4F8557\":\"Keepin It Real (Skit)\",\"SOILPQQ12AB017E82A\":\"Sohna Nee Sohna Data\",\"SOWTBJW12AC468AC6E\":\"Broken-Down Merry-Go-Round\",\"SOBZBAZ12A6D4F8742\":\"Spanish Grease\",\"SOGXHEG12AB018653E\":\"It Makes No Difference Now\",\"SOITUXQ12AB0183AE9\":\"Now's The Time To Fall In Love\",\"SOUVRGZ12A58A77F90\":\"Mangos\",\"SODLSHA12AAF3B29F2\":\"Choro\",\"SOFXNQP12AB0184F1A\":\"Hate\"},\"Country\":{\"SOZVMJI12AB01808AF\":\"Synthetic Dream\",\"SOBLGCN12AB0183212\":\"James (Hold The Ladder Steady)\",\"SOVYKGO12AB0187199\":\"Crazy Mixed Up World\",\"SORSECL12AB0182267\":\"Midnight Swim\",\"SOHGKJW12AC468A8BD\":\"Sabor Guajiro\",\"SOINBCU12A6D4F94C0\":\"Human Cannonball\",\"SOMLZER12A8C134C00\":\"Baby Is Blue\",\"SOAVCCW12A8C13FAD4\":\"Love And Wealth\",\"SOYTSKQ12A8C1373B4\":\"Did You See Jackie Robinson Hit That Ball?\",\"SOJQGJR12AB017FF87\":\"Katherine\"},\"Electronic\":{\"SOPNRDT12AB018A2D8\":\"Charisma\",\"SOQVMXR12A81C21483\":\"Salt In NYC\",\"SOMEUQK12AB017A8FF\":\"Fuego Caliente\",\"SOZVLXO12AF72ABCDB\":\"D Shuffle Jam (Part 2)\",\"SOVZIXG12AC3DF5331\":\"Murderous Style\",\"SOGTVGQ12A8C13952B\":\"You Are!\",\"SOQTUQL12AB01839D7\":\"Anita Love's\",\"SODIWEO12AB018479E\":\"Apogee (ft. TechTonic)\",\"SOAUXQN12AB018D2F6\":\"Mercury & Solace\",\"SOOXHKU12AB0187180\":\"Hello\"},\"Jazz\":{\"SOOGFBZ12AC3DF7FF2\":\"In A Subtle Way\",\"SOMZCVH12AB0182B8E\":\"Stream\",\"SOIJXXM12A8C1416D6\":\"Rosemary Recalls\",\"SOBLTSM12A6D4F9EEA\":\"A Distant Voice\",\"SOAGHMH12AB0180285\":\"Ma Louise\",\"SOJDYQY12AB0188C97\":\"I See The Boys Of Summer\",\"SOFUBCP12A8C13D5EF\":\"Biding Her Time\",\"SOLGRUN12A8C140543\":\"2190 Dias Contigo\",\"SOFGQAP12AB0185946\":\"Dearly Beloved\",\"SOFQGCQ12AB0183609\":\"Still\"},\"Pop/Rock\":{\"SOOKISK12AB0180667\":\"Hey Daddy\",\"SOVGBEK12A8C134E04\":\"4 Sea Interludes Storm\",\"SORJOMF12AB0186F78\":\"I Need You Now\",\"SOLKTEK12AB018BE1A\":\"Walk Away\",\"SODYZIU12A8C13FBD9\":\"Shadows That Move\",\"SOZWHVZ12A6D4F90E7\":\"Kite Live from Sydney\",\"SOQAHUW12AB0181F04\":\"Loco (Crazy)\",\"SOVHRBI12A81C22E02\":\"Ever Be\",\"SOEETVW12A6D4F93B8\":\"Teacher's Pet\",\"SODWXQV12A6310F10D\":\"English Summer Rain\"},\"Rap\":{\"SOYALFX12A8C143637\":\"Honey I Sugar Pie\",\"SOCMHBT12A8C13D634\":\"Phylyps Trak II/II\",\"SODKPPT12A8AE46A99\":\"De Tree Little Pigs\",\"SOESWTI12AB0184D39\":\"Sta giu' (feat. Dj Rockdrive)\",\"SOUHRQN12A58A7C12A\":\"Welcome 2 Detroit\",\"SONCPQC12A58A7D3A7\":\"Spark\",\"SOTUGCE12AB0181409\":\"Destroy Babylon\",\"SOSTONA12A81C22B18\":\"Alimony\",\"SOITFPA12A670201C6\":\"Killing\",\"SOGINAY12A8C138666\":\"Full Of Voices\"},\"R&B\":{\"SOCUSNQ12A8C138A86\":\"Never Gonna Be The Same\",\"SODPMBU12A58A7B076\":\"Fat Tuesday\",\"SOEGCMJ12A58A7B0E6\":\"Plastic People (Original Mix)\",\"SORBDNB12AAF3B1B4E\":\"U Complete Me (Blues Version - Bonus Track)\",\"SOYJFCZ12A8C137848\":\"Memories\",\"SOSBLBE12AC3DFB104\":\"Gimme Legs\",\"SOXCEGC12AB018A0BD\":\"Beautiful When You're Green\",\"SOYGIBP12A67020AF7\":\"Magic\",\"SOKJCDI12A81C22EB0\":\"Imagination\",\"SOYPQHV12A67ADF698\":\"Now I Got A Woman\"}}";
        listDataChild = new Gson().fromJson(seedList, new TypeToken<HashMap<String, HashMap<String,String>>>(){}.getType());
        listDataHeader = new ArrayList<String>(listDataChild.keySet());

//        listDataChild = new HashMap<String, HashMap<String,String>>();

        // Adding child data
//        listDataHeader.add("Top 250");
//        listDataHeader.add("Now Showing");
//        listDataHeader.add("Coming Soon..");
//
//        // Adding child data
//        HashMap<String,String> top250 = new HashMap<String,String>();
//        top250.put("The Shawshank Redemption","one");
//        top250.put("The Godfather","one");
//        top250.put("The Godfather: Part II","one");
//        top250.put("Pulp Fiction","one");
//        top250.put("The Good, the Bad and the Ugly","one");
//        top250.put("The Dark Knight","one");
//        top250.put("12 Angry Men","one");
//
//        HashMap<String,String> nowShowing = new HashMap<String,String>();
//        nowShowing.put("The Shawshank Redemption","one");
//        nowShowing.put("The Godfather","one");
//        nowShowing.put("The Godfather: Part II","one");
//        nowShowing.put("Pulp Fiction","one");
//        nowShowing.put("The Good, the Bad and the Ugly","one");
//        nowShowing.put("The Dark Knight","one");
//        nowShowing.put("12 Angry Men","one");
//
//        HashMap<String,String> comingSoon = new HashMap<String,String>();
//        comingSoon.put("The Shawshank Redemption","one");
//        comingSoon.put("The Godfather","one");
//        comingSoon.put("The Godfather: Part II","one");
//        comingSoon.put("Pulp Fiction","one");
//        comingSoon.put("The Good, the Bad and the Ugly","one");
//        comingSoon.put("The Dark Knight","one");
//        comingSoon.put("12 Angry Men","one");
//
//        listDataChild.put(listDataHeader.get(0), top250); // Header, Child data
//        listDataChild.put(listDataHeader.get(1), nowShowing);
//        listDataChild.put(listDataHeader.get(2), comingSoon);
    }
}
