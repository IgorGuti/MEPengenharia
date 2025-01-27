package com.example.myapplication
import android.content.Intent
import android.os.Bundle
import android.Manifest
import android.content.pm.PackageManager
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.ui.repository.MqttJobIntentService
import com.example.myapplication.ui.repository.UserViewModel
import com.google.android.material.navigation.NavigationView
import android.widget.Toast
import com.example.myapplication.ui.fragment.AboutFragment

private const val REQUEST_CODE_POST_NOTIFICATIONS = 1

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

        // Verifica se a permissão para exibir notificações foi concedida
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // Solicita permissão ao usuário
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), REQUEST_CODE_POST_NOTIFICATIONS)
        } else {
            // A permissão já foi concedida, podemos enfileirar o serviço MQTT
            enqueueMqttService()
        }

        // Observa mudanças no LoginResponse (caso seja necessário para enfileirar o serviço MQTT após login)
        userViewModel.loginResponse.observe(this) { loginResponse ->
            // Aqui você pode enfileirar o serviço MQTT assim que necessário
            // Exemplo de como enfileirar o serviço
            enqueueMqttService()
        }
    }

    private fun enqueueMqttService() {
        // Cria um Intent para o MqttJobIntentService
        val intent = Intent(this, MqttJobIntentService::class.java)

        // Enfileira o serviço
        MqttJobIntentService.enqueueWork(this, intent)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CODE_POST_NOTIFICATIONS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permissão concedida, agora podemos enfileirar o serviço MQTT
                enqueueMqttService()
            } else {
                // Permissão negada
                Toast.makeText(this, "Permissão negada. Não será possível exibir notificações.", Toast.LENGTH_SHORT).show()
            }
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
