package com.yurtcu.operareddittask;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.yurtcu.operareddittask.model.OutmostData;
import com.yurtcu.operareddittask.model.Child;
import com.yurtcu.operareddittask.model.RedditInfo;

public class MainActivity extends AppCompatActivity implements LoadJSONTask.Listener,
        AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    private ListView mListView;
    private SwipeRefreshLayout swipeRefreshLayout;

    private static final String URL = "https://www.reddit.com/r/gaming/top.json?limit=50";

    private List<HashMap<String, String>> mRedditInfoMapList = new ArrayList<>();

    private static final String KEY_TITLE = "title";
    private static final String KEY_SCORE = "score";
    private static final String KEY_SUBREDDIT = "subreddit";
    private static final String KEY_URL = "url";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (ListView) findViewById(R.id.list_view);
        mListView.setOnItemClickListener(this);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);
                                        new LoadJSONTask(MainActivity.this).execute(URL);
                                    }
                                }
        );
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        new LoadJSONTask(MainActivity.this).execute(URL);
    }

    @Override
    public void onLoaded(OutmostData data) {
        mRedditInfoMapList.clear();
        for (Child child : data.getChildren()) {
            RedditInfo redditInfo = child.getData();
            HashMap<String, String> map = new HashMap<>();

            map.put(KEY_TITLE, redditInfo.getTitle());
            map.put(KEY_SCORE, redditInfo.getScore());
            map.put(KEY_SUBREDDIT, redditInfo.getSubreddit());
            map.put(KEY_URL, redditInfo.getUrl());

            mRedditInfoMapList.add(map);
        }

        loadListView();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onError(int errMessageId) {
        swipeRefreshLayout.setRefreshing(false);
        Toast.makeText(this, errMessageId, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intnt = new Intent(Intent.ACTION_VIEW);
        String url = mRedditInfoMapList.get(i).get(KEY_URL);

        intnt.setData(Uri.parse(url));
        startActivity(intnt);
    }

    private void loadListView() {
        ListAdapter adapter = new SimpleAdapter(MainActivity.this, mRedditInfoMapList,
                R.layout.list_item,
                new String[] { KEY_TITLE, KEY_SCORE, KEY_SUBREDDIT },
                new int[] { R.id.title,R.id.score, R.id.subreddit });

        mListView.setAdapter(adapter);
    }
}
