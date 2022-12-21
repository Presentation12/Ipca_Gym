using LayerBOL.Models;
using System.Data;
using System.Data.SqlClient;

namespace LayerDAL.Services
{
    public class ClienteService
    {
        public static async Task<List<Cliente>> GetAllService(string sqlDataSource)
        {
            string query = @"select * from dbo.Cliente";
            List<Cliente> clientes = new List<Cliente>();

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
                            Cliente cliente = new Cliente();

                            cliente.id_cliente = Convert.ToInt32(dataReader["id_cliente"]);
                            cliente.id_ginasio = Convert.ToInt32(dataReader["id_ginasio"]);
                            if (!Convert.IsDBNull(dataReader["id_plano_nutricional"]))
                            {
                                cliente.id_plano_nutricional = Convert.ToInt32(dataReader["id_plano_nutricional"]);
                            }
                            else
                            {
                                cliente.id_plano_nutricional = null;
                            }
                            cliente.nome = dataReader["nome"].ToString();
                            cliente.mail = dataReader["mail"].ToString();
                            cliente.telemovel = Convert.ToInt32(dataReader["telemovel"]);
                            cliente.pass_salt = dataReader["pass_salt"].ToString();
                            cliente.pass_hash = dataReader["pass_hash"].ToString();
                            if (!Convert.IsDBNull(dataReader["peso"]))
                            {
                                cliente.peso = Convert.ToDouble(dataReader["peso"]);
                            }
                            else
                            {
                                cliente.peso = null;
                            }
                            if (!Convert.IsDBNull(dataReader["altura"]))
                            {
                                cliente.altura = Convert.ToInt32(dataReader["altura"]);
                            }
                            else
                            {
                                cliente.altura = null;
                            }
                            if (!Convert.IsDBNull(dataReader["gordura"]))
                            {
                                cliente.gordura = Convert.ToDouble(dataReader["gordura"]);
                            }
                            else
                            {
                                cliente.gordura = null;
                            }
                            if (!Convert.IsDBNull(dataReader["foto_perfil"]))
                            {
                                cliente.foto_perfil = dataReader["foto_perfil"].ToString();
                            }
                            else
                            {
                                cliente.foto_perfil = null;
                            }
                            cliente.estado = dataReader["estado"].ToString();

                            clientes.Add(cliente);
                        }

                        dataReader.Close();
                        databaseConnection.Close();
                    }
                }

                return clientes;
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
                Console.WriteLine(ex.ToString());
                return null;
            }
        }

        public static async Task<Cliente> GetByIDService(string sqlDataSource, int targetID)
        {
            string query = @"
                            select * from dbo.Cliente where id_cliente = @id_cliente";
            try
            {
                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        Console.WriteLine(targetID);
                        myCommand.Parameters.AddWithValue("id_cliente", targetID);

                        using (SqlDataReader reader = myCommand.ExecuteReader())
                        {
                            reader.Read();

                            Cliente targetCliente = new Cliente();
                            targetCliente.id_cliente = reader.GetInt32(0);
                            targetCliente.id_ginasio = reader.GetInt32(1);
                            if (!Convert.IsDBNull(reader["id_plano_nutricional"]))
                            {
                                targetCliente.id_plano_nutricional = reader.GetInt32(2);
                            }
                            else
                            {
                                targetCliente.id_plano_nutricional = null;
                            }
                            targetCliente.nome = reader.GetString(3);
                            targetCliente.mail = reader.GetString(4);
                            targetCliente.telemovel = reader.GetInt32(5);
                            targetCliente.pass_salt = reader.GetString(6);
                            targetCliente.pass_hash = reader.GetString(7);

                            if (!Convert.IsDBNull(reader["peso"]))
                            {
                                targetCliente.peso = reader.GetDouble(8);
                            }
                            else
                            {
                                targetCliente.peso = null;
                            }

                            if (!Convert.IsDBNull(reader["altura"]))
                            {
                                targetCliente.altura = reader.GetInt32(9);
                            }
                            else
                            {
                                targetCliente.altura = null;
                            }

                            if (!Convert.IsDBNull(reader["gordura"]))
                            {
                                targetCliente.gordura = reader.GetInt32(10);
                            }
                            else
                            {
                                targetCliente.gordura = null;
                            }

                            if (!Convert.IsDBNull(reader["foto_perfil"]))
                            {
                                targetCliente.foto_perfil = reader.GetString(11);
                            }
                            else
                            {
                                targetCliente.foto_perfil = null;
                            }
                            targetCliente.estado = reader.GetString(12);

                            reader.Close();
                            databaseConnection.Close();

                            return targetCliente;
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

        public static async Task<bool> PostService(string sqlDataSource, Cliente newCliente)
        {
            string query = @"
                            insert into dbo.Cliente (id_ginasio, id_plano_nutricional, nome, mail, telemovel, pass_salt, pass_hash, peso, altura, gordura, foto_perfil, estado)
                            values (@id_ginasio, @id_plano_nutricional, @nome, @mail, @telemovel, @pass_salt, @pass_hash, @peso, @altura, @gordura, @foto_perfil, @estado)";

            try
            {
                SqlDataReader dataReader;
                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        myCommand.Parameters.AddWithValue("id_ginasio", newCliente.id_ginasio);

                        if (newCliente.id_plano_nutricional != null) myCommand.Parameters.AddWithValue("id_plano_nutricional", newCliente.id_plano_nutricional);
                        else myCommand.Parameters.AddWithValue("id_plano_nutricional", DBNull.Value);

                        myCommand.Parameters.AddWithValue("nome", newCliente.nome);
                        myCommand.Parameters.AddWithValue("mail", newCliente.mail);
                        myCommand.Parameters.AddWithValue("telemovel", newCliente.telemovel);
                        myCommand.Parameters.AddWithValue("pass_salt", newCliente.pass_salt);
                        myCommand.Parameters.AddWithValue("pass_hash", newCliente.pass_hash);

                        if (newCliente.peso != null) myCommand.Parameters.AddWithValue("peso", newCliente.peso);
                        else myCommand.Parameters.AddWithValue("peso", DBNull.Value);

                        if (newCliente.altura != null) myCommand.Parameters.AddWithValue("altura", newCliente.altura);
                        else myCommand.Parameters.AddWithValue("altura", DBNull.Value);

                        if (newCliente.gordura != null) myCommand.Parameters.AddWithValue("gordura", newCliente.gordura);
                        else myCommand.Parameters.AddWithValue("gordura", DBNull.Value);

                        if (!string.IsNullOrEmpty(newCliente.foto_perfil)) myCommand.Parameters.AddWithValue("foto_perfil", newCliente.foto_perfil);
                        else myCommand.Parameters.AddWithValue("foto_perfil", DBNull.Value);

                        myCommand.Parameters.AddWithValue("estado", newCliente.estado);
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

        public static async Task<bool> PatchService(string sqlDataSource, Cliente cliente, int targetID)
        {
            string query = @"
                            update dbo.Classificacao 
                            set id_ginasio = @id_ginasio, 
                            id_plano_nutricional = @id_plano_nutricional, 
                            nome = @nome,
                            mail = @mail,
                            telemovel = @telemovel,
                            pass_salt = @pass_salt,
                            pass_hash = @pass_hash,
                            peso = @peso,
                            altura = @altura,
                            gordura = @gordura,
                            foto_perfil = @foto_perfil,
                            estado = @estado,
                            where id_cliente = @id_cliente";
            try
            {
                Cliente clienteAtual = await GetByIDService(sqlDataSource, targetID);
                SqlDataReader dataReader;

                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        myCommand.Parameters.AddWithValue("id_cliente", cliente.id_cliente != 0 ? cliente.id_cliente : clienteAtual.id_cliente);
                        myCommand.Parameters.AddWithValue("id_ginasio", cliente.id_ginasio != 0 ? cliente.id_ginasio : clienteAtual.id_ginasio);
                        myCommand.Parameters.AddWithValue("id_plano_nutricional", cliente.id_plano_nutricional != 0 ? cliente.id_plano_nutricional : clienteAtual.id_plano_nutricional);
                        myCommand.Parameters.AddWithValue("nome", !string.IsNullOrEmpty(cliente.nome) ? cliente.nome : clienteAtual.nome);
                        myCommand.Parameters.AddWithValue("mail", !string.IsNullOrEmpty(cliente.mail) ? cliente.mail : clienteAtual.mail);
                        myCommand.Parameters.AddWithValue("telemovel", cliente.telemovel != 0 ? cliente.telemovel : clienteAtual.telemovel);
                        myCommand.Parameters.AddWithValue("pass_hash", !string.IsNullOrEmpty(cliente.pass_hash) ? cliente.pass_hash : clienteAtual.pass_hash);
                        myCommand.Parameters.AddWithValue("pass_salt", !string.IsNullOrEmpty(cliente.pass_salt) ? cliente.pass_salt : clienteAtual.pass_salt);
                        myCommand.Parameters.AddWithValue("peso", cliente.peso != (double)0 ? cliente.peso : clienteAtual.peso);
                        myCommand.Parameters.AddWithValue("altura", cliente.altura != 0 ? cliente.altura : clienteAtual.altura);
                        myCommand.Parameters.AddWithValue("gordura", cliente.gordura != (double)0 ? cliente.gordura : clienteAtual.gordura);
                        myCommand.Parameters.AddWithValue("foto_perfil", !string.IsNullOrEmpty(cliente.foto_perfil) ? cliente.foto_perfil : clienteAtual.foto_perfil);
                        myCommand.Parameters.AddWithValue("estado", !string.IsNullOrEmpty(cliente.estado) ? cliente.estado : clienteAtual.estado);

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
            string query = @"
                            delete from dbo.Cliente 
                            where id_cliente = @id_cliente";

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
