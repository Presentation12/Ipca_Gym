using LayerBLL.Utils;
using LayerBOL.Models;
using Microsoft.AspNetCore.Mvc;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Numerics;
using System.Text;
using System.Threading.Tasks;
using LayerDAL.Services;

namespace LayerBLL.Logics
{
    public class PedidoLogic
    {
        public static async Task<Response> GetAllLogic(string sqlDataSource)
        {
            Response response = new Response();
            List<Pedido> pedidoList = await PedidoService.GetAllService(sqlDataSource);

            if (pedidoList.Count != 0)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Lista de pedidos obtida com sucesso";
                response.Data = new JsonResult(pedidoList);
            }

            return response;
        }

        public static async Task<Response> GetByIDLogic(string sqlDataSource, int targetID)
        {
            Response response = new Response();
            Pedido pedido = await PedidoService.GetByIDService(sqlDataSource, targetID);

            if (pedido != null)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Pedido obtido com sucesso!";
                response.Data = new JsonResult(pedido);
            }

            return response;
        }

        public static async Task<Response> PostLogic(string sqlDataSource, Pedido newPedido)
        {
            Response response = new Response();
            bool creationResult = await PedidoService.PostService(sqlDataSource, newPedido);

            if (creationResult)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Success!";
                response.Data = new JsonResult("Pedido criado com sucesso!");
            }

            return response;
        }

        public static async Task<Response> PatchLogic(string sqlDataSource, Pedido pedido, int targetID)
        {
            Response response = new Response();
            bool updateResult = await PedidoService.PatchService(sqlDataSource, pedido, targetID);

            if (updateResult)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Success!";
                response.Data = new JsonResult("Pedido alterado com sucesso!");
            }

            return response;
        }

        public static async Task<Response> DeleteLogic(string sqlDataSource, int targetID)
        {
            Response response = new Response();
            bool deleteResult = await PedidoService.DeleteService(sqlDataSource, targetID);

            if (deleteResult)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Success!";
                response.Data = new JsonResult("Pedido apagado com sucesso!");
            }

            return response;
        }
    }
}
