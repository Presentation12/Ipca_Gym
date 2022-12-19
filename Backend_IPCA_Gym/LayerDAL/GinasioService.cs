using LayerBOL.Models;
using System.Data;
using System.Data.SqlClient;

namespace LayerDAL
{
    public class GinasioService
    {
        public static async Task<List<Ginasio>> GetGinasiosService(string sqlDataSource)
        {
            string query = @"select * from dbo.Ginasio";
            List<Ginasio> ginasios = new List<Ginasio>();

            SqlDataReader dataReader;
            using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
            {
                databaseConnection.Open();
                using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                {
                    dataReader = myCommand.ExecuteReader();
                    while (dataReader.Read())
                    {
                        Ginasio ginasio = new Ginasio();

                        ginasio.id_ginasio = Convert.ToInt32(dataReader["id_ginasio"]);
                        ginasio.estado = dataReader["estado"].ToString();
                        ginasio.instituicao = dataReader["instituicao"].ToString();
                        if (!Convert.IsDBNull(dataReader["foto_ginasio"]))
                        {
                            ginasio.foto_ginasio = dataReader["foto_ginasio"].ToString();
                        }
                        else
                        {
                            ginasio.foto_ginasio = null;
                        }
                        ginasio.contacto = Convert.ToInt32(dataReader["contacto"]);

                        ginasios.Add(ginasio);
                    }

                    dataReader.Close();
                    databaseConnection.Close();
                }
            }

            return ginasios;
        }
    }
}
