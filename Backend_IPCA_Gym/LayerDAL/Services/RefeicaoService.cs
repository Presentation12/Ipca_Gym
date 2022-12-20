using LayerBOL.Models;
using System.Data;
using System.Data.SqlClient;


namespace LayerDAL.Services
{
    public class RefeicaoService
    {
        public static async Task<List<Refeicao>> GetAllService(string sqlDataSource)
        {
            string query = @"select * from dbo.Refeicao";
            List<Refeicao> refeicoes = new List<Refeicao>();

            SqlDataReader dataReader;
            using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
            {
                databaseConnection.Open();
                using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                {
                    dataReader = myCommand.ExecuteReader();
                    while (dataReader.Read())
                    {
                        Refeicao refeicao = new Refeicao();

                        refeicao.id_refeicao = Convert.ToInt32(dataReader["id_refeicao"]);
                        refeicao.id_plano_nutricional = Convert.ToInt32(dataReader["id_plano_nutricional"]);
                        refeicao.descricao = dataReader["descricao"].ToString();
                        refeicao.hora = (TimeSpan)dataReader["hora"];
                        if (!Convert.IsDBNull(dataReader["foto_refeicao"]))
                        {
                            refeicao.foto_refeicao = dataReader["foto_refeicao"].ToString();
                        }
                        else
                        {
                            refeicao.foto_refeicao = null;
                        }


                        refeicoes.Add(refeicao);
                    }

                    dataReader.Close();
                    databaseConnection.Close();
                }
            }

            return refeicoes;
        }
    }
}
