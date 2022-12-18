using Backend_IPCA_Gym.Models;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Npgsql;
using System.Data;
using System.Data.SqlClient;
using System.Numerics;
using System.Runtime.Intrinsics.Arm;

namespace Backend_IPCA_Gym.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class FuncionarioController : ControllerBase
    {
        private readonly IConfiguration _configuration;
        public FuncionarioController(IConfiguration configuration)
        {
            _configuration = configuration;
        }

        [HttpGet]
        public IActionResult GetAll()
        {
            string query = @"
                            select * from dbo.Funcionario";
            List<Funcionario> funcionarios = new List<Funcionario>();

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
                        Funcionario funcionario = new Funcionario();

                        funcionario.id_funcionario = Convert.ToInt32(dataReader["id_funcionario"]);
                        funcionario.id_ginasio = Convert.ToInt32(dataReader["id_ginasio"]);
                        funcionario.nome = dataReader["nome"].ToString();
                        funcionario.is_admin = Convert.ToByte(dataReader["is_admin"]);
                        funcionario.codigo = Convert.ToInt32(dataReader["codigo"]);
                        funcionario.pass_salt = dataReader["pass_salt"].ToString();
                        funcionario.pass_hash = dataReader["pass_hash"].ToString();
                        funcionario.estado = dataReader["estado"].ToString();

                        funcionarios.Add(funcionario);
                    }

                    dataReader.Close();
                    databaseConnection.Close();
                }
            }

            return new JsonResult(funcionarios);
        }

        [HttpGet("{targetID}")]
        public IActionResult GetByID(int targetID)
        {
            string query = @"
                            select * from dbo.Funcionario where id_funcionario = @id_funcionario";

            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
            {
                databaseConnection.Open();
                using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                {
                    Console.WriteLine(targetID);
                    myCommand.Parameters.AddWithValue("id_funcionario", targetID);

                    using (SqlDataReader reader = myCommand.ExecuteReader())
                    {
                        reader.Read();

                        Funcionario targetFuncionario = new Funcionario();
                        targetFuncionario.id_funcionario = reader.GetInt32(0);
                        targetFuncionario.id_ginasio = reader.GetInt32(1);
                        targetFuncionario.nome = reader.GetString(2);
                        targetFuncionario.is_admin = reader.GetByte(3);
                        targetFuncionario.codigo = reader.GetInt32(4);
                        targetFuncionario.pass_salt = reader.GetString(5);
                        targetFuncionario.pass_hash = reader.GetString(6);
                        targetFuncionario.estado = reader.GetString(7);

                        reader.Close();
                        databaseConnection.Close();

                        return new JsonResult(targetFuncionario);
                    }
                }
            }

            return new JsonResult("Erro");
        }

        [HttpPost]
        public IActionResult Post(Funcionario newFuncionario)
        {
            string query = @"
                            insert into dbo.Funcionario (id_ginasio, nome, is_admin, codigo, pass_salt, pass_hash, estado)
                            values (@id_ginasio, @nome, @is_admin, @codigo, @pass_salt, @pass_hash, @estado)";

            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            SqlDataReader dataReader;
            using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
            {
                databaseConnection.Open();
                using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                {
                    myCommand.Parameters.AddWithValue("id_ginasio", newFuncionario.id_ginasio);
                    myCommand.Parameters.AddWithValue("nome", newFuncionario.nome);
                    myCommand.Parameters.AddWithValue("is_admin", newFuncionario.is_admin);
                    myCommand.Parameters.AddWithValue("codigo", newFuncionario.codigo);
                    myCommand.Parameters.AddWithValue("pass_salt", newFuncionario.pass_salt);
                    myCommand.Parameters.AddWithValue("pass_hash", newFuncionario.pass_hash);
                    myCommand.Parameters.AddWithValue("estado", newFuncionario.estado);

                    dataReader = myCommand.ExecuteReader();

                    dataReader.Close();
                    databaseConnection.Close();
                }
            }

            return new JsonResult("Funcionário adicionado com sucesso");
        }

        [HttpPatch]
        public IActionResult Update(Funcionario funcionario)
        {
            string query = @"
                            update dbo.Funcionario 
                            set id_ginasio = @id_ginasio, 
                            nome = @nome, 
                            is_admin = @is_admin,
                            codigo = @codigo,
                            pass_salt = @pass_salt,
                            pass_hash = @pass_hash,
                            estado = @estado
                            where id_funcionario = @id_funcionario";

            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            SqlDataReader dataReader;
            using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
            {
                databaseConnection.Open();
                using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                {
                    myCommand.Parameters.AddWithValue("id_ginasio", funcionario.id_ginasio);
                    myCommand.Parameters.AddWithValue("nome", funcionario.nome);
                    myCommand.Parameters.AddWithValue("is_admin", funcionario.is_admin);
                    myCommand.Parameters.AddWithValue("codigo", funcionario.codigo);
                    myCommand.Parameters.AddWithValue("pass_salt", funcionario.pass_salt);
                    myCommand.Parameters.AddWithValue("pass_hash", funcionario.pass_hash);
                    myCommand.Parameters.AddWithValue("estado", funcionario.estado);

                    dataReader = myCommand.ExecuteReader();

                    dataReader.Close();
                    databaseConnection.Close();
                }
            }

            return new JsonResult("Funcionario atualizado com sucesso");
        }

        [HttpDelete("{targetID}")]
        public IActionResult Delete(int targetID)
        {
            string query = @"
                            delete from dbo.Funcionario 
                            where id_funcionario = @id_funcionario";

            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            SqlDataReader dataReader;
            using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
            {
                databaseConnection.Open();
                using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                {
                    myCommand.Parameters.AddWithValue("id_funcionario", targetID);
                    dataReader = myCommand.ExecuteReader();

                    dataReader.Close();
                    databaseConnection.Close();
                }
            }

            return new JsonResult("Funcionário apagado com sucesso");
        }
    }
}
