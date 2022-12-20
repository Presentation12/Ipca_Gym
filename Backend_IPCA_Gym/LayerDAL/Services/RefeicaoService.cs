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

            try
            {
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
            catch (Exception ex)
            {
                Console.WriteLine(ex.Message);
                return null;
            }
        }

        public static async Task<Refeicao> GetByIDService(string sqlDataSource, int targetID)
        {
            string query = @"select * from dbo.Refeicao where id_refeicao = @id_refeicao";

            try
            {
                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        Console.WriteLine(targetID);
                        myCommand.Parameters.AddWithValue("id_refeicao", targetID);

                        using (SqlDataReader reader = myCommand.ExecuteReader())
                        {
                            reader.Read();

                            Refeicao targetRefeicao = new Refeicao();
                            targetRefeicao.id_refeicao = reader.GetInt32(0);
                            targetRefeicao.id_plano_nutricional = reader.GetInt32(1);
                            targetRefeicao.descricao = reader.GetString(2);
                            targetRefeicao.hora = reader.GetTimeSpan(3);

                            if (!Convert.IsDBNull(reader["foto_refeicao"]))
                            {
                                targetRefeicao.foto_refeicao = reader.GetString(4);
                            }
                            else
                            {
                                targetRefeicao.foto_refeicao = null;
                            }

                            reader.Close();
                            databaseConnection.Close();

                            return targetRefeicao;
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

        public static async Task<bool> PostService(string sqlDataSource, Refeicao newRefeicao)
        {
            string query = @"
                            insert into dbo.Refeicao (id_plano_nutricional, descricao, hora, foto_refeicao)
                            values (@id_plano_nutricional, @descricao, @hora, @foto_refeicao)";

            try
            {
                SqlDataReader dataReader;

                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        myCommand.Parameters.AddWithValue("id_plano_nutricional", newRefeicao.id_plano_nutricional);
                        myCommand.Parameters.AddWithValue("descricao", newRefeicao.descricao);
                        myCommand.Parameters.AddWithValue("hora", newRefeicao.hora);

                        if (!string.IsNullOrEmpty(newRefeicao.foto_refeicao)) myCommand.Parameters.AddWithValue("foto_refeicao", newRefeicao.foto_refeicao);
                        else myCommand.Parameters.AddWithValue("foto_refeicao", string.Empty);

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

        public static async Task<bool> PatchService(string sqlDataSource, Refeicao refeicao, int targetID)
        {
            string query = @"
                            update dbo.Refeicao 
                            set id_plano_nutricional = @id_plano_nutricional, 
                            descricao = @descricao,
                            hora = @hora,
                            foto_refeicao = @foto_refeicao
                            where id_refeicao = @id_refeicao";

            try
            {
                Refeicao refeicaoAtual = await GetByIDService(sqlDataSource, targetID);
                SqlDataReader dataReader;

                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        if (refeicao.id_refeicao != null) myCommand.Parameters.AddWithValue("id_refeicao", refeicao.id_refeicao);
                        else myCommand.Parameters.AddWithValue("id_refeicao", refeicaoAtual.id_refeicao);

                        if (refeicao.id_plano_nutricional != null) myCommand.Parameters.AddWithValue("id_plano_nutricional", refeicao.id_plano_nutricional);
                        else myCommand.Parameters.AddWithValue("id_plano_nutricional", refeicaoAtual.id_plano_nutricional);

                        if (!string.IsNullOrEmpty(refeicao.descricao)) myCommand.Parameters.AddWithValue("descricao", refeicao.descricao);
                        else myCommand.Parameters.AddWithValue("descricao", refeicaoAtual.descricao);

                        if (refeicao.hora != null) myCommand.Parameters.AddWithValue("hora", refeicao.hora);
                        else myCommand.Parameters.AddWithValue("hora", refeicaoAtual.hora);

                        if (!string.IsNullOrEmpty(refeicao.foto_refeicao)) myCommand.Parameters.AddWithValue("foto_refeicao", refeicao.foto_refeicao);
                        else myCommand.Parameters.AddWithValue("foto_refeicao", refeicaoAtual.foto_refeicao);

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
                            delete from dbo.Refeicao 
                            where id_refeicao = @id_refeicao";

            try
            {
                SqlDataReader dataReader;
                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        myCommand.Parameters.AddWithValue("id_refeicao", targetID);
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
