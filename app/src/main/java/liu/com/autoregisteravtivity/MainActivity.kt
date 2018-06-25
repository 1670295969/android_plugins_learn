package liu.com.autoregisteravtivity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import liu.com.aspectj.AspectJAno

class MainActivity : AppCompatActivity() {

    @AspectJAno("haha")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tv.setOnClickListener {
            startActivity(Intent(this,AptActivity::class.java))
        }
    }
}
