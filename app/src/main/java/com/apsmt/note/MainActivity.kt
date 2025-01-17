package com.apsmt.note

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.apsmt.note.databinding.ActivityMainBinding
import de.hdodenhof.circleimageview.CircleImageView

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var toggle: ActionBarDrawerToggle

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var adapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            R.string.open,
            R.string.close
        )

        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val header = binding.navDrawer.getHeaderView(0)
        val profile = header.findViewById<CircleImageView>(R.id.imvProfile)
        profile.setOnClickListener {
            Intent(Intent.ACTION_GET_CONTENT).also{
                it.type = "image/*"
                startActivityForResult(it, 0)
            }
        }

        binding.navDrawer.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.mi_profile -> Toast.makeText(
                    this,
                    "You click on Profile",
                    Toast.LENGTH_SHORT
                ).show()
            }

            true

        }

        databaseHelper = DatabaseHelper(this)

        binding.addFloatingBtn.setOnClickListener {
            Intent(this, AddNoteActivity::class.java).also {
                startActivity(it)
            }
        }

        adapter = NoteAdapter(databaseHelper.readAllNote(), this)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == 0 && resultCode == RESULT_OK) {
            val image = data?.data
            val header = binding.navDrawer.getHeaderView(0)
            val profile = header.findViewById<CircleImageView>(R.id.imvProfile)

            profile.setImageURI(image)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onResume() {
        super.onResume()
        adapter.refreshData(databaseHelper.readAllNote())
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}