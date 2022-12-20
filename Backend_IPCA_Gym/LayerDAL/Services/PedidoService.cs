﻿using LayerBOL.Models;
using System.Data;
using System.Data.SqlClient;

namespace LayerDAL.Services
{
    public class PedidoService
    {
        public static async Task<List<Pedido>> GetAllService(string sqlDataSource)
        {
            string query = @"select * from dbo.Pedido";

            try
            {
                List<Pedido> pedidos = new List<Pedido>();
                SqlDataReader dataReader;

                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        dataReader = myCommand.ExecuteReader();
                        while (dataReader.Read())
                        {
                            Pedido pedido = new Pedido();

                            pedido.id_pedido = Convert.ToInt32(dataReader["id_pedido"]);
                            pedido.id_cliente = Convert.ToInt32(dataReader["id_cliente"]);
                            pedido.data_pedido = Convert.ToDateTime(dataReader["data_pedido"]);
                            pedido.estado = dataReader["estado"].ToString();

                            pedidos.Add(pedido);
                        }

                        dataReader.Close();
                        databaseConnection.Close();
                    }
                }

                return pedidos;
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.Message);
                return null;
            }
        }

        public static async Task<Pedido> GetByIDService(string sqlDataSource, int targetID)
        {
            string query = @"select * from dbo.Pedido where id_pedido = @id_pedido";

            try
            {
                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        Console.WriteLine(targetID);
                        myCommand.Parameters.AddWithValue("id_pedido", targetID);

                        using (SqlDataReader reader = myCommand.ExecuteReader())
                        {
                            reader.Read();

                            Pedido targetPedido = new Pedido();
                            targetPedido.id_pedido = reader.GetInt32(0);
                            targetPedido.id_cliente = reader.GetInt32(1);
                            targetPedido.estado = reader.GetString(2);
                            targetPedido.data_pedido = reader.GetDateTime(3);

                            reader.Close();
                            databaseConnection.Close();

                            return targetPedido;
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

        public static async Task<bool> PostService(string sqlDataSource, Pedido newPedido)
        {
            string query = @"
                            insert into dbo.Pedido (id_cliente, data_pedido, estado)
                            values (@id_cliente, @data_pedido, @estado)";

            try
            {
                SqlDataReader dataReader;

                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        myCommand.Parameters.AddWithValue("id_cliente", newPedido.id_cliente);
                        myCommand.Parameters.AddWithValue("data_pedido", Convert.ToDateTime(newPedido.data_pedido));
                        myCommand.Parameters.AddWithValue("estado", newPedido.estado);

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

        public static async Task<bool> PatchService(string sqlDataSource, Pedido pedido, int targetID)
        {
            string query = @"
                            update dbo.Pedido 
                            set id_cliente = @id_cliente, 
                            data_pedido = @data_pedido,
                            estado = @estado
                            where id_pedido = @id_pedido";

            try
            {
                Pedido pedidoAtual = await GetByIDService(sqlDataSource, targetID);
                SqlDataReader dataReader;

                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        if (pedido.id_pedido != null) myCommand.Parameters.AddWithValue("id_pedido", pedido.id_pedido);
                        else myCommand.Parameters.AddWithValue("id_pedido", pedidoAtual.id_pedido);

                        if (pedido.id_cliente != null) myCommand.Parameters.AddWithValue("id_cliente", pedido.id_cliente);
                        else myCommand.Parameters.AddWithValue("id_cliente", pedidoAtual.id_cliente);

                        if (pedido.data_pedido != null) myCommand.Parameters.AddWithValue("data_pedido", Convert.ToDateTime(pedido.data_pedido));
                        else myCommand.Parameters.AddWithValue("data_pedido", Convert.ToDateTime(pedidoAtual.data_pedido));

                        if (!string.IsNullOrEmpty(pedido.estado)) myCommand.Parameters.AddWithValue("estado", pedido.estado);
                        else myCommand.Parameters.AddWithValue("estado", pedidoAtual.estado);

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
                            delete from dbo.Pedido 
                            where id_pedido = @id_pedido";

            try
            {
                SqlDataReader dataReader;

                using (SqlConnection databaseConnection = new SqlConnection(sqlDataSource))
                {
                    databaseConnection.Open();
                    using (SqlCommand myCommand = new SqlCommand(query, databaseConnection))
                    {
                        myCommand.Parameters.AddWithValue("id_pedido", targetID);
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
