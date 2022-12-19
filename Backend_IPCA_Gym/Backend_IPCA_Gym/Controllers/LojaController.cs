using Backend_IPCA_Gym.Models;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Npgsql;
using System.Data;
using System.Data.SqlClient;
using System.Numerics;
using System.Runtime.Intrinsics.Arm;

namespace Backend_IPCA_Gym.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class LojaController : ControllerBase
    {
        private readonly IConfiguration _configuration;
        public LojaController(IConfiguration configuration)
        {
            _configuration = configuration;
        }

        [HttpGet]
        public IActionResult GetAll()
        {
            string query = @"
                            select * from dbo.Loja";
            List<Loja> produtos = new List<Loja>();

            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
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

            return new JsonResult(produtos);
        }

        [HttpGet("{targetID}")]
        public IActionResult GetByID(int targetID)
        {
            string query = @"
                            select * from dbo.Loja where id_produto = @id_produto";

            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
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
                        targetLoja.foto_produto = reader.GetString(7);

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

                        return new JsonResult(targetLoja);
                    }
                }
            }

            return new JsonResult("Erro");
        }

        [HttpPost]
        public IActionResult Post(Loja newProduto)
        {
            string query = @"
                            insert into dbo.Loja (id_ginasio, nome, tipo_produto, preco, descricao, estado, foto_produto, quantidade)
                            values (@id_ginasio, @nome, @tipo_produto, @preco, @descricao, @estado, @foto_produto, @quantidade)";

            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
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

            return new JsonResult("Produto adicionado com sucesso");
        }

        [HttpPatch]
        public IActionResult Update(Loja produto)
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

            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
            SqlDataReader dataReader;
            using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
            {
                databaseConnection.Open();
                using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                {
                    myCommand.Parameters.AddWithValue("id_produto", produto.id_produto);
                    myCommand.Parameters.AddWithValue("id_ginasio", produto.id_ginasio);
                    myCommand.Parameters.AddWithValue("nome", produto.nome);
                    myCommand.Parameters.AddWithValue("tipo_produto", produto.tipo_produto);
                    myCommand.Parameters.AddWithValue("preco", produto.preco);
                    myCommand.Parameters.AddWithValue("descricao", produto.descricao);
                    myCommand.Parameters.AddWithValue("estado", produto.estado);

                    if (!string.IsNullOrEmpty(produto.foto_produto)) myCommand.Parameters.AddWithValue("foto_produto", produto.foto_produto);
                    else myCommand.Parameters.AddWithValue("foto_produto", DBNull.Value);

                    myCommand.Parameters.AddWithValue("quantidade", produto.quantidade);

                    dataReader = myCommand.ExecuteReader();

                    dataReader.Close();
                    databaseConnection.Close();
                }
            }

            return new JsonResult("Produto atualizado com sucesso");
        }

        [HttpDelete("{targetID}")]
        public IActionResult Delete(int targetID)
        {
            string query = @"
                            delete from dbo.Loja 
                            where id_produto = @id_produto";

            string sqlDataSource = _configuration.GetConnectionString("DatabaseLink");
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

            return new JsonResult("Produto apagado com sucesso");
        }
    }
}
