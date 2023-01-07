using LayerBOL.Models;
using System.Data;
using System.Data.SqlClient;

namespace LayerDAL.Services
{
    public class AtividadeService
    {
        #region DEFAULT REQUESTS

        /// <summary>
        /// Leitura dos dados de todas as atividades da base de dados
        /// </summary>
        /// <param name="sqlDataSource">String de conexão á base de dados</param>
        /// <returns>Lista de atividades se uma leitura bem sucedida, null em caso de erro</returns>
        /// <exception cref="SqlException">Ocorre quando há um erro na conexão com a base de dados.</exception>
        /// <exception cref="InvalidCastException">Ocorre quando há um erro na conversão de dados.</exception>
        /// <exception cref="InvalidOperationException">Trata o caso em que ocorreu um erro de leitura dos dados</exception>
        /// <exception cref="FormatException">Ocorre quando há um erro de tipo de dados.</exception>
        /// <exception cref="IndexOutOfRangeException">Trata o caso em que o índice da coluna da base de dados acessado é inválido</exception>
        /// <exception cref="Exception">Ocorre quando ocorre qualquer outro erro.</exception>
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
                          
                            if (dataReader["data_saida"] == DBNull.Value) atividade.data_saida = null;
                            else atividade.data_saida = Convert.ToDateTime(dataReader["data_saida"]);


                            atividades.Add(atividade);
                        }

                        dataReader.Close();
                        databaseConnection.Close();
                    }
                }

                return atividades;
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
        /// Leitura dos dados de uma atividade através do seu id na base de dados
        /// </summary>
        /// <param name="sqlDataSource">String de conexão á base de dados</param>
        /// <param name="targetID">ID da atividade a ser lida</param>
        /// <returns>Atividade se uma leitura bem sucedida, ou null em caso de erro</returns>
        /// <exception cref="SqlException">Ocorre quando há um erro na conexão com a base de dados.</exception>
        /// <exception cref="InvalidCastException">Ocorre quando há um erro na conversão de dados.</exception>
        /// <exception cref="InvalidOperationException">Trata o caso em que ocorreu um erro de leitura dos dados</exception>
        /// <exception cref="FormatException">Ocorre quando há um erro de tipo de dados.</exception>
        /// <exception cref="IndexOutOfRangeException">Trata o caso em que o índice da coluna da base de dados acessado é inválido</exception>
        /// <exception cref="ArgumentNullException">Ocorre quando um parâmetro é nulo.</exception>
        /// <exception cref="Exception">Ocorre quando ocorre qualquer outro erro.</exception>
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

                            if (reader["data_saida"] == DBNull.Value) targetAtividade.data_saida = null;
                            else targetAtividade.data_saida = reader.GetDateTime(4);

                            reader.Close();
                            databaseConnection.Close();

                            return targetAtividade;
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
        /// Inserção dos dados de uma nova atividade na base de dados
        /// </summary>
        /// <param name="sqlDataSource">String de conexão á base de dados</param>
        /// <param name="newAtividade">Objeto com os dados da nova atividade</param>
        /// <returns>True se a escrita dos dados foi bem sucedida, false em caso de erro.</returns>
        /// <exception cref="SqlException">Ocorre quando há um erro na conexão com a base de dados.</exception>
        /// <exception cref="InvalidCastException">Ocorre quando há um erro na conversão de dados.</exception>
        /// <exception cref="FormatException">Ocorre quando há um erro de tipo de dados.</exception>
        /// <exception cref="ArgumentNullException">Ocorre quando um parâmetro é nulo.</exception>
        /// <exception cref="Exception">Ocorre quando ocorre qualquer outro erro.</exception>
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
                        
                        if(newAtividade.data_saida != null)
                            myCommand.Parameters.AddWithValue("data_saida", Convert.ToDateTime(newAtividade.data_saida));
                        else
                            myCommand.Parameters.AddWithValue("data_saida", DBNull.Value);

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
        /// Atualiza os dados de uma atividade através do seu id na base de dados
        /// </summary>
        /// <param name="sqlDataSource">String de conexão á base de dados</param>
        /// <param name="atividade">Objeto com os novos dados da atividade</param>
        /// <param name="targetID">ID da atividade a ser atualizada</param>
        /// <returns>True se a atualização dos dados foi bem sucedida, false caso contrário</returns>
        /// <exception cref="SqlException">Ocorre quando há um erro na conexão com a base de dados.</exception>
        /// <exception cref="InvalidCastException">Ocorre quando há um erro na conversão de dados.</exception>
        /// <exception cref="FormatException">Ocorre quando há um erro de tipo de dados.</exception>
        /// <exception cref="ArgumentNullException">Ocorre quando um parâmetro é nulo.</exception>
        /// <exception cref="Exception">Ocorre quando ocorre qualquer outro erro.</exception>
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

                if (atividadeAtual == null) throw new ArgumentException("Erro ao alterar a atividade", "targetID");

                SqlDataReader dataReader;

                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        myCommand.Parameters.AddWithValue("id_atividade", atividadeAtual.id_atividade);
                        myCommand.Parameters.AddWithValue("id_ginasio", atividade.id_ginasio != 0 ? atividade.id_ginasio : atividadeAtual.id_ginasio);
                        myCommand.Parameters.AddWithValue("id_cliente", atividade.id_cliente != 0 ? atividade.id_cliente : atividadeAtual.id_cliente);
                        myCommand.Parameters.AddWithValue("data_entrada", atividade.data_entrada != DateTime.MinValue ? Convert.ToDateTime(atividade.data_entrada) : Convert.ToDateTime(atividadeAtual.data_entrada));
                        myCommand.Parameters.AddWithValue("data_saida", atividade.data_saida != DateTime.MinValue ? Convert.ToDateTime(atividade.data_saida) : Convert.ToDateTime(atividadeAtual.data_saida));

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
        /// Remoção de uma atividade da base de dados pelo seu ID
        /// </summary>
        /// <param name="sqlDataSource">String de conexão com a base de dados</param>
        /// <param name="targetID">ID da atividade a ser removida</param>
        /// <returns>True se a remoção foi bem sucedida, false em caso de erro</returns>
        /// <exception cref="SqlException">Ocorre quando há um erro na conexão com a base de dados.</exception>
        /// <exception cref="ArgumentNullException">Ocorre quando um parâmetro é nulo.</exception>
        /// <exception cref="Exception">Ocorre quando ocorre qualquer outro erro.</exception>
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



        #endregion

        #region BACKLOG REQUESTS

        /// <summary>
        /// Leitura dos dados de todas as atividades de um ginasio
        /// </summary>
        /// <param name="sqlDataSource">String de conexão á base de dados</param>
        /// <param name="targetID">ID do ginasio em causa</param>
        /// <returns>Lista de atividades se uma leitura bem sucedida, null em caso de erro</returns>
        /// <exception cref="SqlException">Ocorre quando há um erro na conexão com a base de dados.</exception>
        /// <exception cref="InvalidCastException">Ocorre quando há um erro na conversão de dados.</exception>
        /// <exception cref="InvalidOperationException">Trata o caso em que ocorreu um erro de leitura dos dados</exception>
        /// <exception cref="FormatException">Ocorre quando há um erro de tipo de dados.</exception>
        /// <exception cref="IndexOutOfRangeException">Trata o caso em que o índice da coluna da base de dados acessado é inválido</exception>
        /// <exception cref="Exception">Ocorre quando ocorre qualquer outro erro.</exception>
        public static async Task<List<Atividade>> GetAllbyGymService(string sqlDataSource, int targetID)
        {
            string query = @"select * from dbo.Atividade where id_ginasio = @id_ginasio";
            List<Atividade> atividades = new List<Atividade>();

            try
            {
                SqlDataReader dataReader;
                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        myCommand.Parameters.AddWithValue("id_ginasio", targetID);
                        dataReader = myCommand.ExecuteReader();
                        while (dataReader.Read())
                        {
                            Atividade atividade = new Atividade();

                            atividade.id_atividade = Convert.ToInt32(dataReader["id_atividade"]);
                            atividade.id_ginasio = Convert.ToInt32(dataReader["id_ginasio"]);
                            atividade.id_cliente = Convert.ToInt32(dataReader["id_cliente"]);
                            atividade.data_entrada = Convert.ToDateTime(dataReader["data_entrada"]);

                            if (dataReader["data_saida"] == DBNull.Value) atividade.data_saida = null;
                            else atividade.data_saida = Convert.ToDateTime(dataReader["data_saida"]);


                            atividades.Add(atividade);
                        }

                        dataReader.Close();
                        databaseConnection.Close();
                    }
                }

                return atividades;
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
        /// Calculo de estatísticas do ginásio
        /// </summary>
        /// <param name="sqlDataSource">String de conexão á base de dados</param>
        /// <param name="targetID">ID do ginasio em causa</param>
        /// <returns>Lista de atividades se uma leitura bem sucedida, null em caso de erro</returns>
        /// <exception cref="SqlException">Ocorre quando há um erro na conexão com a base de dados.</exception>
        /// <exception cref="InvalidCastException">Ocorre quando há um erro na conversão de dados.</exception>
        /// <exception cref="InvalidOperationException">Trata o caso em que ocorreu um erro de leitura dos dados</exception>
        /// <exception cref="FormatException">Ocorre quando há um erro de tipo de dados.</exception>
        /// <exception cref="IndexOutOfRangeException">Trata o caso em que o índice da coluna da base de dados acessado é inválido</exception>
        /// <exception cref="Exception">Ocorre quando ocorre qualquer outro erro.</exception>
        public static async Task<StatsModel> GetGymStatsService(string sqlDataSource, int targetID)
        {
            try
            {
                List<Atividade> list = new List<Atividade>();
                StatsModel stats = new StatsModel();

                list = await GetAllbyGymService(sqlDataSource, targetID);

                //Calcular current, exits e entries today
                int totalInTodayCounter = 0;
                int totalOutTodayCounter = 0;
                foreach (Atividade a in list)
                {
                    if (a.data_entrada >= DateTime.Today && a.data_entrada < DateTime.Today.AddDays(1))
                    {
                        if (a.data_saida == null)
                        {
                            totalInTodayCounter++;
                        }
                        else
                        {
                            totalInTodayCounter++;
                            totalOutTodayCounter++;
                        }
                    }

                }
                stats.exits = totalOutTodayCounter;
                stats.today = totalInTodayCounter;
                stats.current = totalInTodayCounter - totalOutTodayCounter;

                //Calcular dailyAverage
                List<Atividade> orderedListDays = list.OrderBy(x => x.data_entrada).ToList();

                TimeSpan diff = DateTime.Today.Subtract(orderedListDays[0].data_entrada);
                double diffDays = diff.TotalDays;

                if (diffDays < 1) diffDays = 1.0;

                stats.dailyAverage = list.Count() / diffDays;

                //Calcular monthlyAverage
                List<Atividade> orderedListMonths = list.OrderBy(x => x.data_entrada).ToList();

                TimeSpan diffAux = DateTime.Today.Subtract(orderedListMonths[0].data_entrada);
                double diffDaysMonths = diffAux.TotalDays;
                double diffMonths = diffDaysMonths / 30.44;

                if (diffMonths < 1) diffMonths = 1.0;

                stats.monthlyAverage = list.Count() / diffMonths;

                //Calcular yearTotal
                int totalYearCounter = 0;
                foreach (Atividade a in list)
                {
                    if (a.data_entrada.Year == DateTime.Today.Year)
                        totalYearCounter++;
                }

                stats.yearTotal = totalYearCounter;

                //Calcular maxDay
                int auxCounter = 0;
                int currentMax = 0;

                for (int i = 0; i < list.Count(); i++)
                {
                    if (i + 1 < list.Count)
                    {
                        if (list[i].data_entrada.Date.CompareTo(list[i + 1].data_entrada.Date) == 0)
                            auxCounter++;
                        else
                        {
                            auxCounter++;

                            if (currentMax < auxCounter)
                                currentMax = auxCounter;

                            auxCounter = 0;
                        }
                    }
                    else
                    {
                        auxCounter++;

                        if (currentMax < auxCounter)
                            currentMax = auxCounter;
                    }
                }
                stats.maxDay = currentMax;

                //Calcular maxMonth


                return stats;
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
        /// Leitura dos dados de todas as atividades de um cliente
        /// </summary>
        /// <param name="sqlDataSource">String de conexão á base de dados</param>
        /// <param name="targetID">ID do cliente em causa</param>
        /// <returns>Lista de atividades se uma leitura bem sucedida, null em caso de erro</returns>
        /// <exception cref="SqlException">Ocorre quando há um erro na conexão com a base de dados.</exception>
        /// <exception cref="InvalidCastException">Ocorre quando há um erro na conversão de dados.</exception>
        /// <exception cref="InvalidOperationException">Trata o caso em que ocorreu um erro de leitura dos dados</exception>
        /// <exception cref="FormatException">Ocorre quando há um erro de tipo de dados.</exception>
        /// <exception cref="IndexOutOfRangeException">Trata o caso em que o índice da coluna da base de dados acessado é inválido</exception>
        /// <exception cref="Exception">Ocorre quando ocorre qualquer outro erro.</exception>
        public static async Task<List<Atividade>> GetAllByClienteIDService(string sqlDataSource, int targetID)
        {
            string query = @"select * from dbo.Atividade where id_cliente = @id_cliente";
            List<Atividade> atividades = new List<Atividade>();

            try
            {
                SqlDataReader dataReader;
                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        myCommand.Parameters.AddWithValue("id_cliente", targetID);
                        dataReader = myCommand.ExecuteReader();
                        while (dataReader.Read())
                        {
                            Atividade atividade = new Atividade();

                            atividade.id_atividade = Convert.ToInt32(dataReader["id_atividade"]);
                            atividade.id_ginasio = Convert.ToInt32(dataReader["id_ginasio"]);
                            atividade.id_cliente = Convert.ToInt32(dataReader["id_cliente"]);
                            atividade.data_entrada = Convert.ToDateTime(dataReader["data_entrada"]);

                            if (dataReader["data_saida"] == DBNull.Value) atividade.data_saida = null;
                            else atividade.data_saida = Convert.ToDateTime(dataReader["data_saida"]);


                            atividades.Add(atividade);
                        }

                        dataReader.Close();
                        databaseConnection.Close();
                    }
                }

                return atividades;
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

        #endregion
    }
}
