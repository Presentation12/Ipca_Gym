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
    public class ExercicioController : ControllerBase
    {
        private readonly IConfiguration _configuration;
        public ExercicioController(IConfiguration configuration)
        {
            _configuration = configuration;
        }

        [HttpGet]
        public IActionResult GetAll()
        {
            string query = @"
                            select * from dbo.Exercicio";
            List<Exercicio> exercicios = new List<Exercicio>();

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
                        Exercicio exercicio = new Exercicio();

                        exercicio.id_exercicio = Convert.ToInt32(dataReader["id_exercicio"]);
                        exercicio.id_plano_treino = Convert.ToInt32(dataReader["id_plano_treino"]);
                        exercicio.nome = dataReader["nome"].ToString();
                        exercicio.descricao = dataReader["descricao"].ToString();
                        exercicio.tipo = dataReader["tipo"].ToString();
                        if (!Convert.IsDBNull(dataReader["series"]))
                        {
                            exercicio.series = Convert.ToInt32(dataReader["series"]);
                        }
                        else
                        {
                            exercicio.series = null;
                        }
                        if (!Convert.IsDBNull(dataReader["tempo"]))
                        {
                            exercicio.tempo = (TimeSpan?)dataReader["tempo"];
                        }
                        else
                        {
                            exercicio.tempo = null;
                        }
                        if (!Convert.IsDBNull(dataReader["repeticoes"]))
                        {
                            exercicio.repeticoes = Convert.ToInt32(dataReader["repeticoes"]);
                        }
                        else
                        {
                            exercicio.repeticoes = null;
                        }

                        exercicios.Add(exercicio);
                    }

                    dataReader.Close();
                    databaseConnection.Close();
                }
            }

            return new JsonResult(exercicios);
        }

        [HttpGet("{targetID}")]
        public IActionResult GetByID(int targetID)
        {
            string query = @"
                            select * from dbo.Exercicio where id_exercicio = @id_exercicio";

            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
            {
                databaseConnection.Open();
                using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                {
                    Console.WriteLine(targetID);
                    myCommand.Parameters.AddWithValue("id_exercicio", targetID);

                    using (SqlDataReader reader = myCommand.ExecuteReader())
                    {
                        reader.Read();

                        Exercicio targetExercicio = new Exercicio();
                        targetExercicio.id_exercicio = reader.GetInt32(0);
                        targetExercicio.id_plano_treino = reader.GetInt32(1);
                        targetExercicio.nome = reader.GetString(2);
                        targetExercicio.descricao = reader.GetString(3);
                        targetExercicio.tipo = reader.GetString(4);

                        if (!Convert.IsDBNull(reader["series"]))
                        {
                            targetExercicio.series = reader.GetInt32(5);
                        }
                        else
                        {
                            targetExercicio.series = null;
                        }

                        if (!Convert.IsDBNull(reader["tempo"]))
                        {
                            targetExercicio.tempo = reader.GetTimeSpan(6);
                        }
                        else
                        {
                            targetExercicio.tempo = null;
                        }

                        if (!Convert.IsDBNull(reader["repeticoes"]))
                        {
                            targetExercicio.repeticoes = reader.GetInt32(7);
                        }
                        else
                        {
                            targetExercicio.repeticoes = null;
                        }

                        reader.Close();
                        databaseConnection.Close();

                        return new JsonResult(targetExercicio);
                    }
                }
            }

            return new JsonResult("Erro");
        }

        [HttpPost]
        public IActionResult Post(Exercicio newExercicio)
        {
            string query = @"
                            insert into dbo.Exercicio (id_plano_treino, nome, descricao, tipo, series, tempo, repeticoes)
                            values (@id_plano_treino, @nome, @descricao, @tipo, @series, @tempo, @repeticoes)";

            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            SqlDataReader dataReader;
            using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
            {
                databaseConnection.Open();
                using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                {
                    myCommand.Parameters.AddWithValue("id_plano_treino", newExercicio.id_plano_treino);
                    myCommand.Parameters.AddWithValue("nome", newExercicio.nome);
                    myCommand.Parameters.AddWithValue("descricao", newExercicio.descricao);
                    myCommand.Parameters.AddWithValue("tipo", newExercicio.tipo);

                    if (newExercicio.series != null) myCommand.Parameters.AddWithValue("series", newExercicio.series);
                    else myCommand.Parameters.AddWithValue("series", DBNull.Value);

                    if (newExercicio.tempo != null) myCommand.Parameters.AddWithValue("tempo", newExercicio.tempo);
                    else myCommand.Parameters.AddWithValue("tempo", DBNull.Value);

                    if (newExercicio.repeticoes != null) myCommand.Parameters.AddWithValue("repeticoes", newExercicio.repeticoes);
                    else myCommand.Parameters.AddWithValue("repeticoes", DBNull.Value);

                    dataReader = myCommand.ExecuteReader();

                    dataReader.Close();
                    databaseConnection.Close();
                }
            }

            return new JsonResult("Exercicio adicionado com sucesso");
        }

        [HttpPatch]
        public IActionResult Update(Exercicio exercicio)
        {
            string query = @"
                            update dbo.Exercicio 
                            set id_plano_treino = @id_plano_treino, 
                            nome = @nome, 
                            descricao = @descricao,
                            tipo = @tipo,
                            series = @series,
                            tempo = @tempo,
                            repeticoes = @repeticoes
                            where id_exercicio = @id_exercicio";

            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            SqlDataReader dataReader;
            using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
            {
                databaseConnection.Open();
                using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                {
                    myCommand.Parameters.AddWithValue("id_exercicio", exercicio.id_exercicio);
                    myCommand.Parameters.AddWithValue("id_plano_treino", exercicio.id_plano_treino);
                    myCommand.Parameters.AddWithValue("nome", exercicio.nome);
                    myCommand.Parameters.AddWithValue("descricao", exercicio.descricao);
                    myCommand.Parameters.AddWithValue("tipo", exercicio.tipo);

                    if (exercicio.series != null) myCommand.Parameters.AddWithValue("series", exercicio.series);
                    else myCommand.Parameters.AddWithValue("series", DBNull.Value);

                    if (exercicio.tempo != null) myCommand.Parameters.AddWithValue("altura", exercicio.tempo);
                    else myCommand.Parameters.AddWithValue("altura", DBNull.Value);

                    if (exercicio.repeticoes != null) myCommand.Parameters.AddWithValue("repeticoes", exercicio.repeticoes);
                    else myCommand.Parameters.AddWithValue("repeticoes", DBNull.Value);

                    dataReader = myCommand.ExecuteReader();

                    dataReader.Close();
                    databaseConnection.Close();
                }
            }

            return new JsonResult("Exercicio atualizado com sucesso");
        }

        [HttpDelete("{targetID}")]
        public IActionResult Delete(int targetID)
        {
            string query = @"
                            delete from dbo.Exercicio 
                            where id_exercicio = @id_exercicio";

            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            SqlDataReader dataReader;
            using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
            {
                databaseConnection.Open();
                using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                {
                    myCommand.Parameters.AddWithValue("id_exercicio", targetID);
                    dataReader = myCommand.ExecuteReader();

                    dataReader.Close();
                    databaseConnection.Close();
                }
            }

            return new JsonResult("Exercicio apagado com sucesso");
        }
    }
}
