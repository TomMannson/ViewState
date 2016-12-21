# ViewState
Android "Annotation Processing" library for persist and restore data after Configuration change


Libray to generate Helpers Class with Annotation processing


### Example for ViewData

Annotation Processing is used to Generate `MainActivityBinder` class which save and restore data which should survive Configuration change

```java
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.tommannson.viewstate.ActivityPersistLoader;
import com.tommannson.viewstate.Persistable;

public class MainActivity extends BaseActivity {

    @ViewData
    Integer data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        if(data == null){
          data = 15;
        }
    }

   @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return ActivityPersistLoader.persist(this);
    }

    @Override
    public Object saveCustomState() {
        return MainActivityBinder.persist(this);
    }

    @Override
    public void loadCustomState(Object retainedState) {
        MainActivityBinder.restore(this, retainedState);
    }
}
```

###

### Example for ActivityArg

Annotation Processing is used also to Generate IntentBuilders class which help to create Activity with strongly typed seters
We don't need to interact with Bundle and create public static finals keys generated code do this for us.

```java
public class TestActivity extends AppCompatActivity {

    @ActivityArg
    boolean booleanData;

    @ActivityArg
    CharSequence[] charSeqArray;

    @ActivityArg
    SubParcelable[] parcelableArray;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TestActivityIntentBuilder.getDataFromIntent(this);
        this.toString();
    }
}
```

start of Annotated activity looks like this
```java
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intentForStart = new TestActivityIntentBuilder()
                        .booleanData(true)
                        .charSeqArray(new CharSequence[]{})
                        .parcelableArray(new SubParcelable[]{new SubParcelable()})
                        .build(MainActivity.this);

                startActivity(intentForStart);

            }
        });
    }
}
```

