package vough.example.ipcagym.cliente_classes

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import vough.example.ipcagym.R

class ActivitiesClienteActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cliente_activities)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navbar)

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    Toast.makeText(this@ActivitiesClienteActivity,"Cliente criado com sucesso!", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_favorites -> {
                    Toast.makeText(this@ActivitiesClienteActivity,"Cliente removido com sucesso!", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.nav_search -> {
                    Toast.makeText(this@ActivitiesClienteActivity,"Cliente alterado com sucesso!", Toast.LENGTH_LONG).show()
                    true
                }
                else -> false
            }
        }
    }
}