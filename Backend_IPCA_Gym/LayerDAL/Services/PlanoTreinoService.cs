using LayerBOL.Models;
using System.Data;
using System.Data.SqlClient;

namespace LayerDAL.Services
{
    public class PlanoTreinoService
    {
        public static async Task<List<PlanoTreino>> GetPlanoTreinosService(string sqlDataSource)
        {
            string query = @"select * from dbo.Plano_Treino";
            List<PlanoTreino> planostreino = new List<PlanoTreino>();

            SqlDataReader dataReader;
            using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
            {
                databaseConnection.Open();
                using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                {
                    dataReader = myCommand.ExecuteReader();
                    while (dataReader.Read())
                    {
                        PlanoTreino planotreino = new PlanoTreino();

                        planotreino.id_plano_treino = Convert.ToInt32(dataReader["id_plano_treino"]);
                        planotreino.id_ginasio = Convert.ToInt32(dataReader["id_ginasio"]);
                        planotreino.tipo = dataReader["tipo"].ToString();

                        if (!Convert.IsDBNull(dataReader["foto_plano_treino"]))
                        {
                            planotreino.foto_plano_treino = dataReader["foto_plano_treino"].ToString();

                        }
                        else
                        {
                            planotreino.foto_plano_treino = null;
                        }

                        planostreino.Add(planotreino);
                    }

                    dataReader.Close();
                    databaseConnection.Close();
                }
            }

            return planostreino;
        }
    }
}