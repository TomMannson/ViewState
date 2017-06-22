# ViewState
Android "Annotation Processing" library for persist and restore data after Configuration change


Libray to generate Helpers Class with Annotation processing


### Example for ViewData

#### Simple example

Annotation Processing is used to Generate `MainActivityBinder` class which save and restore data which should survive Configuration change

```java
public class MainActivity extends AppCompatActivity  implements Persistable {

    @ViewData
    Integer data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityPersistLoader.load(this);
        
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

#### Reduction boilerplate code

In version 0.5 we can integrate librare with less effort.. In annotation processor ViewBinder is also generated simplify whole process

```java
public abstract class BaseActivity extends AppCompatActivity implements Persistable {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityPersistLoader.load(this);
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return ActivityPersistLoader.persist(this);
    }

    @Override
    public Object saveCustomState() {
        return ViewBinder.persist(this);
    }

    @Override
    public void loadCustomState(Object retainedState) {
        ViewBinder.restore(this, retainedState);
    }
}
```

On another step you should only  extents  BaseActivity instad of AppCompatActivity

```java
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
}
```

#### Reduction boilerplate code for fragment

Library works as well for fragments we should declarate Base class for Fragment to reduct code duplication

```java
public abstract class BaseFragment extends Fragment implements Persistable {

    @Override
    public Object saveCustomState() {
        return ViewBinder.persist(this);
    }

    @Override
    public void loadCustomState(Object retainedState) {
        ViewBinder.restore(this, retainedState);
    }
}
```

In another step you should only  extents  from you BaseFragment class

```java

public class MainFragment extends BaseFragment {

    @ViewData
    String asd;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(android.R.layout.simple_list_item_1, container, false);
    }
}
```

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

