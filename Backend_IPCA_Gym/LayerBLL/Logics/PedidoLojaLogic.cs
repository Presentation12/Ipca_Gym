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
    public class PedidoLojaLogic
    {
        public static async Task<Response> GetAllLogic(string sqlDataSource)
        {
            Response response = new Response();
            List<PedidoLoja> pedido_lojaList = await PedidoLojaService.GetAllService(sqlDataSource);

            if (pedido_lojaList.Count != 0)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Lista de produto_pedido obtida com sucesso";
                response.Data = new JsonResult(pedido_lojaList);
            }

            return response;
        }

        public static async Task<Response> GetByIDLogic(string sqlDataSource, int targetID)
        {
            Response response = new Response();
            PedidoLoja pedido_loja = await PedidoLojaService.GetByIDService(sqlDataSource, targetID);

            if (pedido_loja != null)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Produto_pedido obtido com sucesso!";
                response.Data = new JsonResult(pedido_loja);
            }

            return response;
        }

        public static async Task<Response> PostLogic(string sqlDataSource, PedidoLoja newPedido_Loja)
        {
            Response response = new Response();
            bool creationResult = await PedidoLojaService.PostService(sqlDataSource, newPedido_Loja);

            if (creationResult)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Success!";
                response.Data = new JsonResult("Produto_pedido criado com sucesso!");
            }

            return response;
        }

        public static async Task<Response> PatchLogic(string sqlDataSource, PedidoLoja pedido_loja, int targetID)
        {
            Response response = new Response();
            bool updateResult = await PedidoLoja.PatchService(sqlDataSource, pedido_loja, targetID);

            if (updateResult)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Success!";
                response.Data = new JsonResult("Produto_pedido alterado com sucesso!");
            }

            return response;
        }

        public static async Task<Response> DeleteLogic(string sqlDataSource, int targetID)
        {
            Response response = new Response();
            bool deleteResult = await PedidoLoja.DeleteService(sqlDataSource, targetID);

            if (deleteResult)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Success!";
                response.Data = new JsonResult("Produto_pedido apagado com sucesso!");
            }

            return response;
        }
    }
}
