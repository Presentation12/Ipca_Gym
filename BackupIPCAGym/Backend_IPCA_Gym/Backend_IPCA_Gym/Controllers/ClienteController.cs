﻿using Backend_IPCA_Gym.Models;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Backend_IPCA_Gym.Models;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Npgsql;
using System.Data;
using System.Data.SqlClient;
using System.Numerics;
using System.Runtime.Intrinsics.Arm;
using System.Drawing;

namespace Backend_IPCA_Gym.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class ClienteController : ControllerBase
    {
        private readonly IConfiguration _configuration;
        public ClienteController(IConfiguration configuration)
        {
            _configuration = configuration;
        }

        protected Cliente GetClienteByID(int targetID)
        {
            string query = @"select * from dbo.Cliente where id_cliente = @id_cliente";

            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
            {
                databaseConnection.Open();
                using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                {
                    Console.WriteLine(targetID);
                    myCommand.Parameters.AddWithValue("id_cliente", targetID);

                    using (SqlDataReader reader = myCommand.ExecuteReader())
                    {
                        reader.Read();

                        Cliente targetCliente = new Cliente();
                        targetCliente.id_cliente = reader.GetInt32(0);
                        targetCliente.id_ginasio = reader.GetInt32(1);
                        if (!Convert.IsDBNull(reader["id_plano_nutricional"]))
                        {
                            targetCliente.id_plano_nutricional = reader.GetInt32(2);
                        }
                        else
                        {
                            targetCliente.id_plano_nutricional = null;
                        }
                        targetCliente.nome = reader.GetString(3);
                        targetCliente.mail = reader.GetString(4);
                        targetCliente.telemovel = reader.GetInt32(5);
                        targetCliente.pass_salt = reader.GetString(6);
                        targetCliente.pass_hash = reader.GetString(7);

                        if (!Convert.IsDBNull(reader["peso"]))
                        {
                            targetCliente.peso = reader.GetDouble(8);
                        }
                        else
                        {
                            targetCliente.peso = null;
                        }

                        if (!Convert.IsDBNull(reader["altura"]))
                        {
                            targetCliente.altura = reader.GetInt32(9);
                        }
                        else
                        {
                            targetCliente.altura = null;
                        }

                        if (!Convert.IsDBNull(reader["gordura"]))
                        {
                            targetCliente.gordura = reader.GetInt32(10);
                        }
                        else
                        {
                            targetCliente.gordura = null;
                        }

                        if (!Convert.IsDBNull(reader["foto_perfil"]))
                        {
                            targetCliente.foto_perfil = reader.GetString(11);
                        }
                        else
                        {
                            targetCliente.foto_perfil = null;
                        }
                        targetCliente.estado = reader.GetString(12);

                        reader.Close();
                        databaseConnection.Close();

                        return targetCliente;
                    }
                }
            }
        }

        [HttpGet]
        public IActionResult GetAll()
        {
            string query = @"
                            select * from dbo.Cliente";
            List<Cliente> clientes = new List<Cliente>();

            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            SqlDataReader dataReader;
            using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
            {
                databaseConnection.Open();
                using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                {
                    dataReader = myCommand.ExecuteReader();
                    while (dataReader.Read())
                    {
                        Cliente cliente = new Cliente();

                        cliente.id_cliente = Convert.ToInt32(dataReader["id_cliente"]);
                        cliente.id_ginasio = Convert.ToInt32(dataReader["id_ginasio"]);
                        if (!Convert.IsDBNull(dataReader["id_plano_nutricional"]))
                        {
                            cliente.id_plano_nutricional = Convert.ToInt32(dataReader["id_plano_nutricional"]);
                        }
                        else
                        {
                            cliente.id_plano_nutricional = null;
                        }
                        cliente.nome = dataReader["nome"].ToString();
                        cliente.mail = dataReader["mail"].ToString();
                        cliente.telemovel = Convert.ToInt32(dataReader["telemovel"]);
                        cliente.pass_salt = dataReader["pass_salt"].ToString();
                        cliente.pass_hash = dataReader["pass_hash"].ToString();
                        if (!Convert.IsDBNull(dataReader["peso"]))
                        {
                            cliente.peso = Convert.ToDouble(dataReader["peso"]);
                        }
                        else
                        {
                            cliente.peso = null;
                        }
                        if (!Convert.IsDBNull(dataReader["altura"]))
                        {
                            cliente.altura = Convert.ToInt32(dataReader["altura"]);
                        }
                        else
                        {
                            cliente.altura = null;
                        }
                        if (!Convert.IsDBNull(dataReader["gordura"]))
                        {
                            cliente.gordura = Convert.ToDouble(dataReader["gordura"]);
                        }
                        else
                        {
                            cliente.gordura = null;
                        }
                        if (!Convert.IsDBNull(dataReader["foto_perfil"]))
                        {
                            cliente.foto_perfil = dataReader["foto_perfil"].ToString();
                        }
                        else
                        {
                            cliente.foto_perfil = null;
                        }
                        cliente.estado = dataReader["estado"].ToString();

                        clientes.Add(cliente);
                    }

                    dataReader.Close();
                    databaseConnection.Close();
                }
            }

            return new JsonResult(clientes);
        }

        [HttpGet("{targetID}")]
        public IActionResult GetByID(int targetID)
        {
            string query = @"
                            select * from dbo.Cliente where id_cliente = @id_cliente";

            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
            {
                databaseConnection.Open();
                using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                {
                    Console.WriteLine(targetID);
                    myCommand.Parameters.AddWithValue("id_cliente", targetID);

                    using (SqlDataReader reader = myCommand.ExecuteReader())
                    {
                        reader.Read();

                        Cliente targetCliente = new Cliente();
                        targetCliente.id_cliente = reader.GetInt32(0);
                        targetCliente.id_ginasio = reader.GetInt32(1);
                        if (!Convert.IsDBNull(reader["id_plano_nutricional"]))
                        {
                            targetCliente.id_plano_nutricional = reader.GetInt32(2);
                        }
                        else
                        {
                            targetCliente.id_plano_nutricional = null;
                        }
                        targetCliente.nome = reader.GetString(3);
                        targetCliente.mail = reader.GetString(4);
                        targetCliente.telemovel = reader.GetInt32(5);
                        targetCliente.pass_salt = reader.GetString(6);
                        targetCliente.pass_hash = reader.GetString(7);
                        
                        if (!Convert.IsDBNull(reader["peso"]))
                        {
                            targetCliente.peso = reader.GetDouble(8);
                        }
                        else
                        {
                            targetCliente.peso = null;
                        }

                        if (!Convert.IsDBNull(reader["altura"]))
                        {
                            targetCliente.altura = reader.GetInt32(9);
                        }
                        else
                        {
                            targetCliente.altura = null;
                        }
                        
                        if (!Convert.IsDBNull(reader["gordura"]))
                        {
                            targetCliente.gordura = reader.GetInt32(10);
                        }
                        else
                        {
                            targetCliente.gordura = null;
                        }
                        
                        if (!Convert.IsDBNull(reader["foto_perfil"]))
                        {
                            targetCliente.foto_perfil = reader.GetString(11);
                        }
                        else
                        {
                            targetCliente.foto_perfil = null;
                        }
                        targetCliente.estado = reader.GetString(12);

                        reader.Close();
                        databaseConnection.Close();

                        return new JsonResult(targetCliente);
                    }
                }
            }

            return new JsonResult("Erro");
        }

        [HttpPost]
        public IActionResult Post(Cliente newCliente)
        {
            string query = @"
                            insert into dbo.Cliente (id_ginasio, id_plano_nutricional, nome, mail, telemovel, pass_salt, pass_hash, peso, altura, gordura, foto_perfil, estado)
                            values (@id_ginasio, @id_plano_nutricional, @nome, @mail, @telemovel, @pass_salt, @pass_hash, @peso, @altura, @gordura, @foto_perfil, @estado)";

            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            SqlDataReader dataReader;
            using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
            {
                databaseConnection.Open();
                using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                {
                    myCommand.Parameters.AddWithValue("id_ginasio", newCliente.id_ginasio);

                    if (newCliente.id_plano_nutricional != null) myCommand.Parameters.AddWithValue("id_plano_nutricional", newCliente.id_plano_nutricional);
                    else myCommand.Parameters.AddWithValue("id_plano_nutricional", DBNull.Value);
                    
                    myCommand.Parameters.AddWithValue("nome", newCliente.nome);
                    myCommand.Parameters.AddWithValue("mail", newCliente.mail);
                    myCommand.Parameters.AddWithValue("telemovel", newCliente.telemovel);
                    myCommand.Parameters.AddWithValue("pass_salt", newCliente.pass_salt);
                    myCommand.Parameters.AddWithValue("pass_hash", newCliente.pass_hash);

                    if (newCliente.peso != null) myCommand.Parameters.AddWithValue("peso", newCliente.peso);
                    else myCommand.Parameters.AddWithValue("peso", DBNull.Value);

                    if (newCliente.altura != null) myCommand.Parameters.AddWithValue("altura", newCliente.altura);
                    else myCommand.Parameters.AddWithValue("altura", DBNull.Value);

                    if (newCliente.gordura != null) myCommand.Parameters.AddWithValue("gordura", newCliente.gordura);
                    else myCommand.Parameters.AddWithValue("gordura", DBNull.Value);

                    if (!string.IsNullOrEmpty(newCliente.foto_perfil)) myCommand.Parameters.AddWithValue("foto_perfil", newCliente.foto_perfil);
                    else myCommand.Parameters.AddWithValue("foto_perfil", DBNull.Value);

                    myCommand.Parameters.AddWithValue("estado", newCliente.estado);
                    dataReader = myCommand.ExecuteReader();

                    dataReader.Close();
                    databaseConnection.Close();
                }
            }

            return new JsonResult("Cliente adicionado com sucesso");
        }

        [HttpPatch("{targetID}")]
        public IActionResult Update(Cliente cliente, int targetID)
        {
            string query = @"
                            update dbo.Classificacao 
                            set id_ginasio = @id_ginasio, 
                            id_plano_nutricional = @id_plano_nutricional, 
                            nome = @nome,
                            mail = @mail,
                            telemovel = @telemovel,
                            pass_salt = @pass_salt,
                            pass_hash = @pass_hash,
                            peso = @peso,
                            altura = @altura,
                            gordura = @gordura,
                            foto_perfil = @foto_perfil,
                            estado = @estado,
                            where id_cliente = @id_cliente";

            Cliente clienteAtual = GetClienteByID(targetID);

            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            SqlDataReader dataReader;
            using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
            {
                databaseConnection.Open();
                using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                {
                    if (cliente.id_cliente != null) myCommand.Parameters.AddWithValue("id_cliente", cliente.id_cliente);
                    else myCommand.Parameters.AddWithValue("id_cliente", clienteAtual.id_cliente);

                    if (cliente.id_ginasio != null) myCommand.Parameters.AddWithValue("id_ginasio", cliente.id_ginasio);
                    else myCommand.Parameters.AddWithValue("id_ginasio", clienteAtual.id_ginasio);

                    if (cliente.id_plano_nutricional != null) myCommand.Parameters.AddWithValue("id_plano_nutricional", cliente.id_plano_nutricional);
                    else myCommand.Parameters.AddWithValue("id_plano_nutricional", clienteAtual.id_plano_nutricional);

                    if (!string.IsNullOrEmpty(cliente.nome)) myCommand.Parameters.AddWithValue("nome", cliente.nome);
                    else myCommand.Parameters.AddWithValue("nome", clienteAtual.nome);

                    if (!string.IsNullOrEmpty(cliente.mail)) myCommand.Parameters.AddWithValue("mail", cliente.mail);
                    else myCommand.Parameters.AddWithValue("mail", clienteAtual.mail);

                    if (cliente.telemovel != null) myCommand.Parameters.AddWithValue("telemovel", cliente.telemovel);
                    else myCommand.Parameters.AddWithValue("telemovel", clienteAtual.telemovel);

                    if (!string.IsNullOrEmpty(cliente.pass_salt)) myCommand.Parameters.AddWithValue("pass_salt", cliente.pass_salt);
                    else myCommand.Parameters.AddWithValue("pass_salt", clienteAtual.pass_salt);

                    if (!string.IsNullOrEmpty(cliente.pass_hash)) myCommand.Parameters.AddWithValue("pass_hash", cliente.pass_hash);
                    else myCommand.Parameters.AddWithValue("pass_hash", clienteAtual.pass_hash);

                    if (cliente.peso != null) myCommand.Parameters.AddWithValue("peso", cliente.peso);
                    else myCommand.Parameters.AddWithValue("peso", clienteAtual.peso);

                    if (cliente.altura != null) myCommand.Parameters.AddWithValue("altura", cliente.altura);
                    else myCommand.Parameters.AddWithValue("altura", clienteAtual.altura);

                    if (cliente.gordura != null) myCommand.Parameters.AddWithValue("gordura", cliente.gordura);
                    else myCommand.Parameters.AddWithValue("gordura", clienteAtual.gordura);

                    if (!string.IsNullOrEmpty(cliente.foto_perfil)) myCommand.Parameters.AddWithValue("foto_perfil", cliente.foto_perfil);
                    else myCommand.Parameters.AddWithValue("foto_perfil", clienteAtual.foto_perfil);

                    if (!string.IsNullOrEmpty(cliente.estado)) myCommand.Parameters.AddWithValue("estado", cliente.estado);
                    else myCommand.Parameters.AddWithValue("estado", clienteAtual.estado);

                    dataReader = myCommand.ExecuteReader();

                    dataReader.Close();
                    databaseConnection.Close();
                }
            }

            return new JsonResult("Classificação atualizada com sucesso");
        }

        [HttpDelete("{targetID}")]
        public IActionResult Delete(int targetID)
        {
            string query = @"
                            delete from dbo.Cliente 
                            where id_cliente = @id_cliente";

            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            SqlDataReader dataReader;
            using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
            {
                databaseConnection.Open();
                using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                {
                    myCommand.Parameters.AddWithValue("id_cliente", targetID);
                    dataReader = myCommand.ExecuteReader();

                    dataReader.Close();
                    databaseConnection.Close();
                }
            }

            return new JsonResult("Cliente apagado com sucesso");
        }
    }
}