using LayerBOL.Models;
using System.Data;
using System.Data.SqlClient;

namespace LayerDAL.Services
{
    public class GinasioService
    {
        public static async Task<List<Ginasio>> GetAllService(string sqlDataSource)
        {
            string query = @"select * from dbo.Ginasio";
            List<Ginasio> ginasios = new List<Ginasio>();

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

        public static async Task<Ginasio> GetByIDService(string sqlDataSource, int targetID)
        {
            string query = @"select * from dbo.Ginasio where id_ginasio = @id_ginasio";

            try
            {
                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        myCommand.Parameters.AddWithValue("id_ginasio", targetID);

                        using (SqlDataReader reader = myCommand.ExecuteReader())
                        {
                            reader.Read();

                            Ginasio targetGinasio = new Ginasio();
                            targetGinasio.id_ginasio = reader.GetInt32(0);
                            targetGinasio.instituicao = reader.GetString(1);
                            targetGinasio.contacto = reader.GetInt32(2);

                            if (!Convert.IsDBNull(reader["foto_ginasio"]))
                            {
                                targetGinasio.foto_ginasio = reader.GetString(3);
                            }
                            else
                            {
                                targetGinasio.foto_ginasio = null;
                            }

                            targetGinasio.estado = reader.GetString(4);

                            reader.Close();
                            databaseConnection.Close();

                            return targetGinasio;
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

        public static async Task<bool> PostService(string sqlDataSource, Ginasio newGinasio)
        {
            string query = @"insert into dbo.Ginasio (instituicao, contacto, foto_ginasio, estado)
                            values (@instituicao, @contacto, @foto_ginasio, @estado)";

            try
            {
                SqlDataReader dataReader;
                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        myCommand.Parameters.AddWithValue("instituicao", newGinasio.instituicao);
                        myCommand.Parameters.AddWithValue("contacto", newGinasio.contacto);
                        if (!string.IsNullOrEmpty(newGinasio.foto_ginasio)) myCommand.Parameters.AddWithValue("foto_ginasio", newGinasio.foto_ginasio);
                        else myCommand.Parameters.AddWithValue("foto_ginasio", DBNull.Value);
                        myCommand.Parameters.AddWithValue("estado", newGinasio.estado);
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

        public static async Task<bool> PatchService(string sqlDataSource, Ginasio ginasio, int targetID)
        {
            string query = @"
                            update dbo.Ginasio 
                            set instituicao = @instituicao, 
                            contacto = @contacto, 
                            foto_ginasio = @foto_ginasio, 
                            estado = @estado
                            where id_ginasio = @id_ginasio";

            try
            {
                Ginasio ginasioAtual = await GetByIDService(sqlDataSource, targetID);
                SqlDataReader dataReader;
            
                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        myCommand.Parameters.AddWithValue("id_ginasio", ginasio.id_ginasio != 0 ? ginasio.id_ginasio : ginasioAtual.id_ginasio);
                        myCommand.Parameters.AddWithValue("instituicao", !string.IsNullOrEmpty(ginasio.instituicao) ? ginasio.instituicao : ginasioAtual.instituicao);
                        myCommand.Parameters.AddWithValue("contacto", ginasio.contacto != 0 ? ginasio.contacto : ginasioAtual.contacto);
                        myCommand.Parameters.AddWithValue("foto_ginasio", !string.IsNullOrEmpty(ginasio.foto_ginasio) ? ginasio.foto_ginasio : ginasioAtual.foto_ginasio);
                        myCommand.Parameters.AddWithValue("estado", !string.IsNullOrEmpty(ginasio.estado) ? ginasio.estado : ginasioAtual.estado);

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

        public static async Task<bool> DeleteService(string sqlDataSource, int targetID)
        {
            string query = @"delete from dbo.Ginasio 
                            where id_ginasio = @id_ginasio";

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
