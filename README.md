# ViewState
Android "Annotation Processing" library for persist and restore data after Configuration change


Libray to generate Helpers Class with Annotation processing



### Example

Here's a (boring) `HelloWorld` class:

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
