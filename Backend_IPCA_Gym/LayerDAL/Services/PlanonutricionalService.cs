using LayerBOL.Models;
using System.Data;
using System.Data.SqlClient;


namespace LayerDAL.Services
{
    public class PlanoNutricionalService
    {
        /// <summary>
        /// Leitura dos dados de todos os planos nutricionais da base de dados
        /// </summary>
        /// <param name="sqlDataSource">String de conexão á base de dados</param>
        /// <returns>Lista de planos nutricionais se uma leitura bem sucedida, null em caso de erro</returns>
        /// <exception cref="SqlException">Ocorre quando há um erro na conexão com a base de dados.</exception>
        /// <exception cref="InvalidCastException">Ocorre quando há um erro na conversão de dados.</exception>
        /// <exception cref="InvalidOperationException">Trata o caso em que ocorreu um erro de leitura dos dados</exception>
        /// <exception cref="FormatException">Ocorre quando há um erro de tipo de dados.</exception>
        /// <exception cref="IndexOutOfRangeException">Trata o caso em que o índice da coluna da base de dados acessado é inválido</exception>
        /// <exception cref="Exception">Ocorre quando ocorre qualquer outro erro.</exception>
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
            catch (SqlException ex)
            {
                Console.WriteLine("Erro na conexão com a base de dados: " + ex.Message);
                return null;
            }
            catch (InvalidCastException ex)
            {
                Console.WriteLine("Erro na conversão de dados: " + ex.Message);
                return null;
            }
            catch (InvalidOperationException ex)
            {
                Console.WriteLine("Erro de leitura dos dados: " + ex.Message);
                return null;
            }
            catch (FormatException ex)
            {
                Console.WriteLine("Erro de tipo de dados: " + ex.Message);
                return null;
            }
            catch (IndexOutOfRangeException ex)
            {
                Console.WriteLine("Erro de acesso a uma coluna da base de dados: " + ex.Message);
                return null;
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.Message);
                return null;
            }
        }

        /// <summary>
        /// Leitura dos dados de um plano nutricional através do seu id na base de dados
        /// </summary>
        /// <param name="sqlDataSource">String de conexão á base de dados</param>
        /// <param name="targetID">ID do plano nutricional a ser lido</param>
        /// <returns>Plano nutricional se uma leitura bem sucedida, ou null em caso de erro</returns>
        /// <exception cref="SqlException">Ocorre quando há um erro na conexão com a base de dados.</exception>
        /// <exception cref="InvalidCastException">Ocorre quando há um erro na conversão de dados.</exception>
        /// <exception cref="InvalidOperationException">Trata o caso em que ocorreu um erro de leitura dos dados</exception>
        /// <exception cref="FormatException">Ocorre quando há um erro de tipo de dados.</exception>
        /// <exception cref="IndexOutOfRangeException">Trata o caso em que o índice da coluna da base de dados acessado é inválido</exception>
        /// <exception cref="ArgumentNullException">Ocorre quando um parâmetro é nulo.</exception>
        /// <exception cref="Exception">Ocorre quando ocorre qualquer outro erro.</exception>
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
            catch (SqlException ex)
            {
                Console.WriteLine("Erro na conexão com a base de dados: " + ex.Message);
                return null;
            }
            catch (InvalidCastException ex)
            {
                Console.WriteLine("Erro na conversão de dados: " + ex.Message);
                return null;
            }
            catch (InvalidOperationException ex)
            {
                Console.WriteLine("Erro de leitura dos dados: " + ex.Message);

                return null;
            }
            catch (FormatException ex)
            {
                Console.WriteLine("Erro de tipo de dados: " + ex.Message);
                return null;
            }
            catch (IndexOutOfRangeException ex)
            {
                Console.WriteLine("Erro de acesso a uma coluna da base de dados: " + ex.Message);
                return null;
            }
            catch (ArgumentNullException ex)
            {
                Console.WriteLine("Erro de parametro inserido nulo: " + ex.Message);
                return null;
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.Message);
                return null;
            }

        }

        /// <summary>
        /// Inserção dos dados de um novo plano nutricional na base de dados
        /// </summary>
        /// <param name="sqlDataSource">String de conexão á base de dados</param>
        /// <param name="newPlanoNutricional">Objeto com os dados do novo plano nutricional</param>
        /// <returns>True se a escrita dos dados foi bem sucedida, false em caso de erro.</returns>
        /// <exception cref="SqlException">Ocorre quando há um erro na conexão com a base de dados.</exception>
        /// <exception cref="InvalidCastException">Ocorre quando há um erro na conversão de dados.</exception>
        /// <exception cref="FormatException">Ocorre quando há um erro de tipo de dados.</exception>
        /// <exception cref="ArgumentNullException">Ocorre quando um parâmetro é nulo.</exception>
        /// <exception cref="Exception">Ocorre quando ocorre qualquer outro erro.</exception>
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
            catch (SqlException ex)
            {
                Console.WriteLine("Erro na conexão com a base de dados: " + ex.Message);
                return false;
            }
            catch (InvalidCastException ex)
            {
                Console.WriteLine("Erro na conversão de dados: " + ex.Message);
                return false;
            }
            catch (FormatException ex)
            {
                Console.WriteLine("Erro de tipo de dados: " + ex.Message);
                return false;
            }
            catch (ArgumentNullException ex)
            {
                Console.WriteLine("Erro de parametro inserido nulo: " + ex.Message);
                return false;
            }
            catch (Exception ex)
            {
                Console.Write(ex.ToString());
                return false;
            }
        }

        /// <summary>
        /// Atualiza os dados de um plano nutricional através do seu id na base de dados
        /// </summary>
        /// <param name="sqlDataSource">String de conexão á base de dados</param>
        /// <param name="planoNutricional">Objeto com os novos dados da plano nutricional</param>
        /// <param name="targetID">ID do plano nutricional a ser atualizado</param>
        /// <returns>True se a atualização dos dados foi bem-sucedida, false caso contrário</returns>
        /// <exception cref="SqlException">Ocorre quando há um erro na conexão com a base de dados.</exception>
        /// <exception cref="InvalidCastException">Ocorre quando há um erro na conversão de dados.</exception>
        /// <exception cref="FormatException">Ocorre quando há um erro de tipo de dados.</exception>
        /// <exception cref="ArgumentNullException">Ocorre quando um parâmetro é nulo.</exception>
        /// <exception cref="Exception">Ocorre quando ocorre qualquer outro erro.</exception>
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
            catch (SqlException ex)
            {
                Console.WriteLine("Erro na conexão com a base de dados: " + ex.Message);
                return false;
            }
            catch (InvalidCastException ex)
            {
                Console.WriteLine("Erro na conversão de dados: " + ex.Message);
                return false;
            }
            catch (FormatException ex)
            {
                Console.WriteLine("Erro de tipo de dados: " + ex.Message);
                return false;
            }
            catch (ArgumentNullException ex)
            {
                Console.WriteLine("Erro de parametro inserido nulo: " + ex.Message);
                return false;
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.ToString());
                return false;
            }
        }

        /// <summary>
        /// Remoção de um plano nutricional da base de dados pelo seu ID
        /// </summary>
        /// <param name="sqlDataSource">String de conexão com a base de dados</param>
        /// <param name="targetID">ID do plano nutricional a ser removido</param>
        /// <returns>True se a remoção foi bem sucedida, false em caso de erro</returns>
        /// <exception cref="SqlException">Ocorre quando há um erro na conexão com a base de dados.</exception>
        /// <exception cref="ArgumentNullException">Ocorre quando um parâmetro é nulo.</exception>
        /// <exception cref="Exception">Ocorre quando ocorre qualquer outro erro.</exception>
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
            catch (SqlException ex)
            {
                Console.WriteLine("Erro na conexão com a base de dados: " + ex.Message);
                return false;
            }
            catch (ArgumentNullException ex)
            {
                Console.WriteLine("Erro de parametro inserido nulo: " + ex.Message);
                return false;
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.ToString());
                return false;
            }
        }
    }
}
