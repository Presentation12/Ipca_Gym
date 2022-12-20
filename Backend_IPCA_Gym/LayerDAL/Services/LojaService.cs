using LayerBOL.Models;
using System.Data;
using System.Data.SqlClient;

namespace LayerDAL.Services
{
    public class LojaService
    {
        public static async Task<List<Loja>> GetAllService(string sqlDataSource)
        {
            string query = @"select * from dbo.Loja";

            try
            {
                List<Loja> produtos = new List<Loja>();
                SqlDataReader dataReader;

                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        dataReader = myCommand.ExecuteReader();
                        while (dataReader.Read())
                        {
                            Loja produto = new Loja();

                            produto.id_produto = Convert.ToInt32(dataReader["id_produto"]);
                            produto.id_ginasio = Convert.ToInt32(dataReader["id_ginasio"]);
                            produto.nome = dataReader["nome"].ToString();
                            produto.descricao = dataReader["descricao"].ToString();
                            produto.preco = Convert.ToDouble(dataReader["preco"]);
                            produto.tipo_produto = dataReader["tipo_produto"].ToString();
                            produto.estado = dataReader["estado"].ToString();
                            if (!Convert.IsDBNull(dataReader["foto_produto"]))
                            {
                                produto.foto_produto = dataReader["foto_produto"].ToString();
                            }
                            else
                            {
                                produto.foto_produto = null;
                            }
                            produto.quantidade = Convert.ToInt32(dataReader["quantidade"]);

                            produtos.Add(produto);
                        }

                        dataReader.Close();
                        databaseConnection.Close();
                    }
                }

                return produtos;
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.Message);
                return null;
            }
        }

        public static async Task<Loja> GetByIDService(string sqlDataSource, int targetID)
        {
            string query = @"select * from dbo.Loja where id_produto = @id_produto";

            try
            {
                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        Console.WriteLine(targetID);
                        myCommand.Parameters.AddWithValue("id_produto", targetID);

                        using (SqlDataReader reader = myCommand.ExecuteReader())
                        {
                            reader.Read();

                            Loja targetLoja = new Loja();
                            targetLoja.id_produto = reader.GetInt32(0);
                            targetLoja.id_ginasio = reader.GetInt32(1);
                            targetLoja.nome = reader.GetString(2);
                            targetLoja.tipo_produto = reader.GetString(3);
                            targetLoja.preco = reader.GetDouble(4);
                            targetLoja.descricao = reader.GetString(5);
                            targetLoja.estado = reader.GetString(6);

                            if (!Convert.IsDBNull(reader["foto_produto"]))
                            {
                                targetLoja.foto_produto = reader.GetString(7);
                            }
                            else
                            {
                                targetLoja.foto_produto = null;
                            }

                            targetLoja.quantidade = reader.GetInt32(8);

                            reader.Close();
                            databaseConnection.Close();

                            return targetLoja;
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

        public static async Task<bool> PostService(string sqlDataSource, Loja newProduto)
        {
            string query = @"
                            insert into dbo.Loja (id_ginasio, nome, tipo_produto, preco, descricao, estado, foto_produto, quantidade)
                            values (@id_ginasio, @nome, @tipo_produto, @preco, @descricao, @estado, @foto_produto, @quantidade)";

            try
            {
                SqlDataReader dataReader;

                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        myCommand.Parameters.AddWithValue("id_ginasio", newProduto.id_ginasio);
                        myCommand.Parameters.AddWithValue("nome", newProduto.nome);
                        myCommand.Parameters.AddWithValue("tipo_produto", newProduto.tipo_produto);
                        myCommand.Parameters.AddWithValue("preco", newProduto.preco);
                        myCommand.Parameters.AddWithValue("descricao", newProduto.descricao);
                        myCommand.Parameters.AddWithValue("estado", newProduto.estado);

                        if (!string.IsNullOrEmpty(newProduto.foto_produto)) myCommand.Parameters.AddWithValue("foto_produto", newProduto.foto_produto);
                        else myCommand.Parameters.AddWithValue("foto_produto", DBNull.Value);

                        myCommand.Parameters.AddWithValue("quantidade", newProduto.quantidade);

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

        public static async Task<bool> PatchService(string sqlDataSource, Loja produto, int targetID)
        {
            string query = @"
                            update dbo.Loja 
                            set id_ginasio = @id_ginasio, 
                            nome = @nome, 
                            tipo_produto = @tipo_produto,
                            preco = @preco,
                            descricao = @descricao,
                            estado = @estado,
                            foto_produto = @foto_produto,
                            quantidade = @quantidade
                            where id_produto = @id_produto";

            try
            {
                Loja produtoAtual = await GetByIDService(sqlDataSource, targetID);
                SqlDataReader dataReader;

                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        if (produto.id_produto != null) myCommand.Parameters.AddWithValue("id_produto", produto.id_produto);
                        else myCommand.Parameters.AddWithValue("id_produto", produtoAtual.id_produto);

                        if (produto.id_ginasio != null) myCommand.Parameters.AddWithValue("id_ginasio", produto.id_ginasio);
                        else myCommand.Parameters.AddWithValue("id_ginasio", produtoAtual.id_ginasio);

                        if (!string.IsNullOrEmpty(produto.nome)) myCommand.Parameters.AddWithValue("nome", produto.nome);
                        else myCommand.Parameters.AddWithValue("nome", produtoAtual.nome);

                        if (!string.IsNullOrEmpty(produto.tipo_produto)) myCommand.Parameters.AddWithValue("tipo_produto", produto.tipo_produto);
                        else myCommand.Parameters.AddWithValue("tipo_produto", produtoAtual.tipo_produto);

                        if (produto.preco != null) myCommand.Parameters.AddWithValue("preco", produto.preco);
                        else myCommand.Parameters.AddWithValue("preco", produtoAtual.preco);

                        if (!string.IsNullOrEmpty(produto.descricao)) myCommand.Parameters.AddWithValue("descricao", produto.descricao);
                        else myCommand.Parameters.AddWithValue("descricao", produtoAtual.descricao);

                        if (!string.IsNullOrEmpty(produto.estado)) myCommand.Parameters.AddWithValue("estado", produto.estado);
                        else myCommand.Parameters.AddWithValue("estado", produtoAtual.estado);

                        if (!string.IsNullOrEmpty(produto.foto_produto)) myCommand.Parameters.AddWithValue("foto_produto", produto.foto_produto);
                        else myCommand.Parameters.AddWithValue("foto_produto", produtoAtual.foto_produto);

                        if (produto.quantidade != null) myCommand.Parameters.AddWithValue("quantidade", produto.quantidade);
                        else myCommand.Parameters.AddWithValue("quantidade", produtoAtual.quantidade);

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
                            delete from dbo.Loja 
                            where id_produto = @id_produto";

            try
            {
                SqlDataReader dataReader;

                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        myCommand.Parameters.AddWithValue("id_produto", targetID);
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
