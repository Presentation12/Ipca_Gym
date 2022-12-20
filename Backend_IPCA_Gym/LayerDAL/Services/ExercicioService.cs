using LayerBOL.Models;
using System.Data;
using System.Data.SqlClient;


namespace LayerDAL.Services
{
    public class ExercicioService
    {
        public static async Task<List<Exercicio>> GetAllService(string sqlDataSource)
        {
            string query = @"select * from dbo.Exercicio";
            List<Exercicio> exercicios = new List<Exercicio>();

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
                        if (!Convert.IsDBNull(dataReader["foto_exercicio"]))
                        {
                            exercicio.foto_exercicio = dataReader["foto_exercicio"].ToString();
                        }
                        else
                        {
                            exercicio.foto_exercicio = null;
                        }


                        exercicios.Add(exercicio);
                    }

                    dataReader.Close();
                    databaseConnection.Close();
                }
            }
            return exercicios;
        }
    }
}
