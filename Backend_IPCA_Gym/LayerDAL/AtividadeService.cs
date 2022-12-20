using LayerBOL.Models;
using System.Data;
using System.Data.SqlClient;

namespace LayerDAL
{
    public class AtividadeService
    {
        public static async Task<List<Atividade>> GetAtividadesService(string sqlDataSource)
        {
            string query = @"select * from dbo.Atividade";
            List<Atividade> atividades = new List<Atividade>();

            SqlDataReader dataReader;
            using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
            {
                databaseConnection.Open();
                using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                {
                    dataReader = myCommand.ExecuteReader();
                    while (dataReader.Read())
                    {
                        Atividade atividade = new Atividade();

                        atividade.id_atividade = Convert.ToInt32(dataReader["id_atividade"]);
                        atividade.id_ginasio = Convert.ToInt32(dataReader["id_ginasio"]);
                        atividade.id_cliente = Convert.ToInt32(dataReader["id_cliente"]);
                        atividade.data_entrada = Convert.ToDateTime(dataReader["data_entrada"]);
                        atividade.data_saida = Convert.ToDateTime(dataReader["data_saida"]);

                        atividades.Add(atividade);
                    }

                    dataReader.Close();
                    databaseConnection.Close();
                }
            }
            return atividades;
        }
    }
}
