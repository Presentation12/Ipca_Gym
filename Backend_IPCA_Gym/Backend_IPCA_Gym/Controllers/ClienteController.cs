using Backend_IPCA_Gym.Models;
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
    }
}
