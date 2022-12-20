using LayerBOL.Models;
using System.Data;
using System.Data.SqlClient;


namespace LayerDAL.Services
{
    public class PlanoNutricionalService
    {
        public static async Task<List<PlanoNutricional>> GetPlanoNutricionaisService(string sqlDataSource)
        {
            string query = @"select * from dbo.Plano_Nutricional";
            List<PlanoNutricional> planosnutricionais = new List<PlanoNutricional>();

            SqlDataReader dataReader;
            using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
            {
                databaseConnection.Open();
                using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                {
                    dataReader = myCommand.ExecuteReader();
                    while (dataReader.Read())
                    {
                        PlanoNutricional planoNutricional = new PlanoNutricional();

                        planoNutricional.id_plano_nutricional = Convert.ToInt32(dataReader["id_plano_nutricional"]);
                        planoNutricional.id_ginasio = Convert.ToInt32(dataReader["id_ginasio"]);
                        planoNutricional.tipo = dataReader["tipo"].ToString();
                        planoNutricional.calorias = Convert.ToInt32(dataReader["calorias"]);

                        if (!Convert.IsDBNull(dataReader["foto_plano_nutricional"]))
                        {
                            planoNutricional.foto_plano_nutricional = dataReader["foto_plano_nutricional"].ToString();
                        }
                        else
                        {
                            planoNutricional.foto_plano_nutricional = null;
                        }

                        planosnutricionais.Add(planoNutricional);
                    }

                    dataReader.Close();
                    databaseConnection.Close();
                }
            }

            return planosnutricionais;
        }
    }
}
