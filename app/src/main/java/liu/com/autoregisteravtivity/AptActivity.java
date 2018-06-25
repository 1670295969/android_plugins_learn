package liu.com.autoregisteravtivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import liu.com.aspectj.AspectJAno;

public class AptActivity extends AppCompatActivity {
    @AspectJAno("haha")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
