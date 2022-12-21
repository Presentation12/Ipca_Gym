using LayerBOL.Models;
using System.Data;
using System.Data.SqlClient;

namespace LayerDAL.Services
{
    public class PlanoTreinoService
    {
        public static async Task<List<PlanoTreino>> GetAllService(string sqlDataSource)
        {
            string query = @"select * from dbo.Plano_Treino";

            try
            {
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
            catch (Exception ex)
            {
                Console.WriteLine(ex.Message);
                return null;
            }
        }

        public static async Task<PlanoTreino> GetByIDService(string sqlDataSource, int targetID)
        {
            string query = @"select * from dbo.Plano_Treino where id_plano_treino = @id_plano_treino";

            try
            {
                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        Console.WriteLine(targetID);
                        myCommand.Parameters.AddWithValue("id_plano_treino", targetID);

                        using (SqlDataReader reader = myCommand.ExecuteReader())
                        {
                            reader.Read();

                            PlanoTreino targetPlanoTreino = new PlanoTreino();
                            targetPlanoTreino.id_plano_treino = reader.GetInt32(0);
                            targetPlanoTreino.id_ginasio = reader.GetInt32(1);
                            targetPlanoTreino.tipo = reader.GetString(2);

                            if (!Convert.IsDBNull(reader["foto_plano_treino"]))
                            {
                                targetPlanoTreino.foto_plano_treino = reader.GetString(3);
                            }
                            else
                            {
                                targetPlanoTreino.foto_plano_treino = null;
                            }

                            reader.Close();
                            databaseConnection.Close();

                            return targetPlanoTreino;
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

        public static async Task<bool> PostService(string sqlDataSource, PlanoTreino newPlanoTreino)
        {
            string query = @"
                            insert into dbo.Plano_Treino (id_ginasio, tipo, foto_plano_treino)
                            values (@id_ginasio, @tipo, @foto_plano_treino)";

            try
            {
                SqlDataReader dataReader;

                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        myCommand.Parameters.AddWithValue("id_ginasio", newPlanoTreino.id_ginasio);
                        myCommand.Parameters.AddWithValue("tipo", newPlanoTreino.tipo);
                        if (!string.IsNullOrEmpty(newPlanoTreino.foto_plano_treino)) myCommand.Parameters.AddWithValue("foto_plano_treino", newPlanoTreino.foto_plano_treino);
                        else myCommand.Parameters.AddWithValue("foto_plano_treino", DBNull.Value);

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

        public static async Task<bool> PatchService(string sqlDataSource, PlanoTreino planoTreino, int targetID)
        {
            string query = @"
                            update dbo.Plano_Treino 
                            set id_ginasio = @id_ginasio, 
                            tipo = @tipo,
                            foto_plano_treino = @foto_plano_treino
                            where id_plano_treino = @id_plano_treino";

            try
            {
                PlanoTreino planoTreinoAtual = await GetByIDService(sqlDataSource, targetID);
                SqlDataReader dataReader;

                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        myCommand.Parameters.AddWithValue("id_plano_treino", planoTreino.id_plano_treino != 0 ? planoTreino.id_plano_treino : planoTreinoAtual.id_plano_treino);
                        myCommand.Parameters.AddWithValue("id_ginasio", planoTreino.id_ginasio != 0 ? planoTreino.id_ginasio : planoTreinoAtual.id_ginasio);
                        myCommand.Parameters.AddWithValue("tipo", !string.IsNullOrEmpty(planoTreino.tipo) ? planoTreino.tipo : planoTreinoAtual.tipo);
                        myCommand.Parameters.AddWithValue("foto_plano_treino", !string.IsNullOrEmpty(planoTreino.foto_plano_treino) ? planoTreino.foto_plano_treino : planoTreinoAtual.foto_plano_treino);


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
                            delete from dbo.Plano_Treino 
                            where id_plano_treino = @id_plano_treino";

            try
            {
                SqlDataReader dataReader;
                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        myCommand.Parameters.AddWithValue("id_plano_treino", targetID);
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