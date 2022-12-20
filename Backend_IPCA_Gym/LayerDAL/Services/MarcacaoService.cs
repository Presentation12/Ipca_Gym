using LayerBOL.Models;
using System.Data;
using System.Data.SqlClient;

namespace LayerDAL.Services
{
    public class MarcacaoService
    {
        public static async Task<List<Marcacao>> GetMarcacoesService(string sqlDataSource)
        {
            string query = @"select * from dbo.Marcacao";
            List<Marcacao> marcacoes = new List<Marcacao>();

            SqlDataReader dataReader;
            using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
            {
                databaseConnection.Open();
                using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                {
                    dataReader = myCommand.ExecuteReader();
                    while (dataReader.Read())
                    {
                        Marcacao marcacao = new Marcacao();

                        marcacao.id_marcacao = Convert.ToInt32(dataReader["id_marcacao"]);
                        marcacao.id_funcionario = Convert.ToInt32(dataReader["id_funcionario"]);
                        marcacao.id_cliente = Convert.ToInt32(dataReader["id_cliente"]);
                        marcacao.data_marcacao = Convert.ToDateTime(dataReader["data_marcacao"]);
                        marcacao.descricao = dataReader["descricao"].ToString();
                        marcacao.estado = dataReader["estado"].ToString();
                        marcacoes.Add(marcacao);
                    }

                    dataReader.Close();
                    databaseConnection.Close();
                }
            }

            return marcacoes;
        }
    }
}

