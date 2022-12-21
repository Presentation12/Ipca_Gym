using LayerBOL.Models;
using System.Data;
using System.Data.SqlClient;


namespace LayerDAL.Services
{
    public class PlanoNutricionalService
    {
        public static async Task<List<PlanoNutricional>> GetAllService(string sqlDataSource)
        {
            string query = @"select * from dbo.Plano_Nutricional";

            try
            {
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
            catch (Exception ex)
            {
                Console.WriteLine(ex.Message);
                return null;
            }
        }

        public static async Task<PlanoNutricional> GetByIDService(string sqlDataSource, int targetID)
        {
            string query = @"select * from dbo.Plano_Nutricional where id_plano_nutricional = @id_plano_nutricional";

            try
            {
                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        Console.WriteLine(targetID);
                        myCommand.Parameters.AddWithValue("id_plano_nutricional", targetID);

                        using (SqlDataReader reader = myCommand.ExecuteReader())
                        {
                            reader.Read();

                            PlanoNutricional targetPlanoNutricional = new PlanoNutricional();
                            targetPlanoNutricional.id_plano_nutricional = reader.GetInt32(0);
                            targetPlanoNutricional.id_ginasio = reader.GetInt32(1);
                            targetPlanoNutricional.tipo = reader.GetString(2);
                            targetPlanoNutricional.calorias = reader.GetInt32(3);
                            if (!Convert.IsDBNull(reader["foto_plano_nutricional"]))
                            {
                                targetPlanoNutricional.foto_plano_nutricional = reader.GetString(4);
                            }
                            else
                            {
                                targetPlanoNutricional.foto_plano_nutricional = null;
                            }

                            reader.Close();
                            databaseConnection.Close();

                            return targetPlanoNutricional;
                        }
                    }
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.Message);
                return null;
            }

        }

        public static async Task<bool> PostService(string sqlDataSource, PlanoNutricional newPlanoNutricional)
        {
            string query = @"
                            insert into dbo.Plano_Nutricional (id_ginasio, tipo, calorias, foto_plano_nutricional)
                            values (@id_ginasio, @tipo, @calorias, @foto_plano_nutricional)";

            try
            {
                SqlDataReader dataReader;
                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        myCommand.Parameters.AddWithValue("id_ginasio", newPlanoNutricional.id_ginasio);
                        myCommand.Parameters.AddWithValue("tipo", newPlanoNutricional.tipo);
                        myCommand.Parameters.AddWithValue("calorias", newPlanoNutricional.calorias);

                        if (!string.IsNullOrEmpty(newPlanoNutricional.foto_plano_nutricional)) myCommand.Parameters.AddWithValue("foto_plano_nutricional", newPlanoNutricional.foto_plano_nutricional);
                        else myCommand.Parameters.AddWithValue("foto_plano_nutricional", DBNull.Value);

                        dataReader = myCommand.ExecuteReader();

                        dataReader.Close();
                        databaseConnection.Close();
                    }
                }

                return true;
            }
            catch (Exception ex)
            {
                Console.Write(ex.ToString());
                return false;
            }
        }

        public static async Task<bool> PatchService(string sqlDataSource, PlanoNutricional planoNutricional, int targetID)
        {
            string query = @"
                            update dbo.Plano_Nutricional 
                            set id_ginasio = @id_ginasio, 
                            tipo = @tipo,
                            calorias = @calorias,
                            foto_plano_nutricional = @foto_plano_nutricional
                            where id_plano_nutricional = @id_plano_nutricional";

            try
            {
                PlanoNutricional planoNutricionalAtual = await GetByIDService(sqlDataSource, targetID);
                SqlDataReader dataReader;

                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        myCommand.Parameters.AddWithValue("id_plano_nutricional", planoNutricional.id_plano_nutricional != 0 ? planoNutricional.id_plano_nutricional : planoNutricionalAtual.id_plano_nutricional);
                        myCommand.Parameters.AddWithValue("id_ginasio", planoNutricional.id_ginasio != 0 ? planoNutricional.id_ginasio : planoNutricionalAtual.id_ginasio);
                        myCommand.Parameters.AddWithValue("tipo", !string.IsNullOrEmpty(planoNutricional.tipo) ? planoNutricional.tipo : planoNutricionalAtual.tipo);
                        myCommand.Parameters.AddWithValue("calorias", planoNutricional.calorias != 0 ? planoNutricional.calorias : planoNutricionalAtual.calorias);
                        myCommand.Parameters.AddWithValue("foto_plano_nutricional", !string.IsNullOrEmpty(planoNutricional.foto_plano_nutricional) ? planoNutricional.foto_plano_nutricional : planoNutricionalAtual.foto_plano_nutricional);

                        dataReader = myCommand.ExecuteReader();

                        dataReader.Close();
                        databaseConnection.Close();
                    }
                }

                return true;
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.ToString());
                return false;
            }
        }

        public static async Task<bool> DeleteService(string sqlDataSource, int targetID)
        {
            string query = @"
                            delete from dbo.Plano_Nutricional 
                            where id_plano_nutricional = @id_plano_nutricional";

            try
            {
                SqlDataReader dataReader;

                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        myCommand.Parameters.AddWithValue("id_plano_nutricional", targetID);
                        dataReader = myCommand.ExecuteReader();

                        dataReader.Close();
                        databaseConnection.Close();
                    }
                }

                return true;
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.ToString());
                return false;
            }
        }
    }
}
