package com.example.myapplication

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.ui.fragment.AboutFragment
import com.example.myapplication.ui.repository.UserViewModel

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Infla o layout da atividade principal a partir do arquivo XML
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configura a barra de ação (ActionBar) com a barra de ferramentas (Toolbar)
        setSupportActionBar(binding.appBarMain.toolbar)

        // Obtém referências para o DrawerLayout e NavigationView
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView

        // Configura a navegação usando o NavController
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        // Define as configurações da AppBar, especificando os destinos de menu e o DrawerLayout
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_monitorEletrico, R.id.nav_monitorReservatorio
            ), drawerLayout
        )

        // Configura a ActionBar para trabalhar com a navegação
        setupActionBarWithNavController(navController, appBarConfiguration)

        // Configura o NavigationView para trabalhar com a navegação
        navView.setupWithNavController(navController)

        // Observa mudanças no LoginResponse
        userViewModel.loginResponse.observe(this) { loginResponse ->

        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Infla o menu na barra de ação, se estiver presente
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_about -> {
                // Crie uma instância do fragmento AboutFragment
                val aboutFragment = AboutFragment()

                // Use o FragmentManager para substituir o conteúdo principal pelo fragmento AboutFragment
                supportFragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment_content_main, aboutFragment)
                    .addToBackStack(null) // Adicione à pilha de retorno se desejar
                    .commit()

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        // Configura a navegação de retorno na ActionBar
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    companion object {
        // Visibilidade do botão hamburguer do layout drawer
    }
}
