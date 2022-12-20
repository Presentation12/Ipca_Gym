using LayerBOL.Models;
using System.Data;
using System.Data.SqlClient;

namespace LayerDAL
{
    public class ClassificacaoService
    {
        public static async Task<List<Classificacao>> GetClassificacoesService(string sqlDataSource)
        {
            string query = @"select * from dbo.Classificacao";
            List<Classificacao> classificacoes = new List<Classificacao>();

            SqlDataReader dataReader;
            using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
            {
                databaseConnection.Open();
                using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                {
                    dataReader = myCommand.ExecuteReader();
                    while (dataReader.Read())
                    {
                        Classificacao classificacao = new Classificacao();

                        classificacao.id_avaliacao = Convert.ToInt32(dataReader["id_avaliacao"]);
                        classificacao.id_ginasio = Convert.ToInt32(dataReader["id_ginasio"]);
                        classificacao.id_cliente = Convert.ToInt32(dataReader["id_cliente"]);
                        classificacao.avaliacao = Convert.ToInt32(dataReader["avaliacao"]);
                        classificacao.comentario = dataReader["comentario"].ToString();
                        classificacao.data_avaliacao = Convert.ToDateTime(dataReader["data_avaliacao"]);

                        classificacoes.Add(classificacao);
                    }

                    dataReader.Close();
                    databaseConnection.Close();
                }
            }
            return classificacoes;
        }
    }
}
