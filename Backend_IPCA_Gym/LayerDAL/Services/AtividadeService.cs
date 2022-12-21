using LayerBOL.Models;
using System.Data;
using System.Data.SqlClient;

namespace LayerDAL.Services
{
    public class AtividadeService
    {
        public static async Task<List<Atividade>> GetAllService(string sqlDataSource)
        {
            string query = @"select * from dbo.Atividade";
            List<Atividade> atividades = new List<Atividade>();

            try
            {
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
            catch (Exception ex)
            {
                Console.WriteLine(ex.Message);
                return null;
            }
        }

        public static async Task<Atividade> GetByIDService(string sqlDataSource, int targetID)
        {
            string query = @"select * from dbo.Atividade where id_atividade = @id_atividade";

            try
            {
                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        Console.WriteLine(targetID);
                        myCommand.Parameters.AddWithValue("id_atividade", targetID);

                        using (SqlDataReader reader = myCommand.ExecuteReader())
                        {
                            reader.Read();

                            Atividade targetAtividade = new Atividade();
                            targetAtividade.id_atividade = reader.GetInt32(0);
                            targetAtividade.id_ginasio = reader.GetInt32(1);
                            targetAtividade.id_cliente = reader.GetInt32(2);
                            targetAtividade.data_entrada = reader.GetDateTime(3);
                            targetAtividade.data_saida = reader.GetDateTime(4);

                            reader.Close();
                            databaseConnection.Close();

                            return targetAtividade;
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

        public static async Task<bool> PostService(string sqlDataSource, Atividade newAtividade)
        {
            string query = @"insert into dbo.Atividade (id_ginasio, id_cliente, data_entrada, data_saida)
                            values (@id_ginasio, @id_cliente, @data_entrada, @data_saida)";
            try
            {
                SqlDataReader dataReader;
                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        myCommand.Parameters.AddWithValue("id_ginasio", newAtividade.id_ginasio);
                        myCommand.Parameters.AddWithValue("id_cliente", newAtividade.id_cliente);
                        myCommand.Parameters.AddWithValue("data_entrada", Convert.ToDateTime(newAtividade.data_entrada));
                        myCommand.Parameters.AddWithValue("data_saida", Convert.ToDateTime(newAtividade.data_saida));
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

        public static async Task<bool> PatchService(string sqlDataSource, Atividade atividade, int targetID)
        {
            string query = @"
                            update dbo.Atividade 
                            set id_ginasio = @id_ginasio, 
                            id_cliente = @id_cliente, 
                            data_entrada = @data_entrada, 
                            data_saida = @data_saida
                            where id_atividade = @id_atividade";

            try
            {
                Atividade atividadeAtual = await GetByIDService(sqlDataSource, targetID);
                SqlDataReader dataReader;

                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        /*
                        if (atividade.id_atividade.HasValue) myCommand.Parameters.AddWithValue("id_atividade", atividade.id_atividade);
                        else myCommand.Parameters.AddWithValue("id_atividade", atividadeAtual.id_atividade);

                        if (atividade.id_ginasio != null) myCommand.Parameters.AddWithValue("id_ginasio", atividade.id_ginasio);
                        else myCommand.Parameters.AddWithValue("id_ginasio", atividadeAtual.id_ginasio);

                        if (atividade.id_cliente != null) myCommand.Parameters.AddWithValue("id_cliente", atividade.id_cliente);
                        else myCommand.Parameters.AddWithValue("id_cliente", atividadeAtual.id_cliente);

                        if (atividade.data_entrada != null) myCommand.Parameters.AddWithValue("data_entrada", Convert.ToDateTime(atividade.data_entrada));
                        else myCommand.Parameters.AddWithValue("data_entrada", Convert.ToDateTime(atividadeAtual.data_entrada));

                        if (atividade.data_saida != null) myCommand.Parameters.AddWithValue("data_saida", Convert.ToDateTime(atividade.data_saida));
                        else myCommand.Parameters.AddWithValue("data_saida", Convert.ToDateTime(atividadeAtual.data_saida));
                        */

                        //myCommand.Parameters.AddWithValue("id_atividade", atividade.id_atividade ?? atividadeAtual.id_atividade);

                        myCommand.Parameters.AddWithValue("id_atividade", atividade.id_atividade != 0 ? atividade.id_atividade : atividadeAtual.id_atividade);
                        myCommand.Parameters.AddWithValue("id_ginasio", atividade.id_ginasio != 0 ? atividade.id_ginasio : atividadeAtual.id_ginasio);
                        myCommand.Parameters.AddWithValue("id_cliente", atividade.id_cliente != 0 ? atividade.id_cliente : atividadeAtual.id_cliente);
                        myCommand.Parameters.AddWithValue("data_entrada", atividade.data_entrada != DateTime.MinValue ? Convert.ToDateTime(atividade.data_entrada) : Convert.ToDateTime(atividadeAtual.data_entrada));
                        myCommand.Parameters.AddWithValue("data_entrada", atividade.data_entrada != DateTime.MinValue ? Convert.ToDateTime(atividade.data_entrada) : Convert.ToDateTime(atividadeAtual.data_entrada));

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
                            delete from dbo.Atividade 
                            where id_atividade = @id_atividade";

            try
            {
                SqlDataReader dataReader;

                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        myCommand.Parameters.AddWithValue("id_atividade", targetID);
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
