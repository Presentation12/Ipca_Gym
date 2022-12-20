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
        public static async Task<Response> GetPedidosLogic(string sqlDataSource)
        {
            Response response = new Response();
            List<Pedido> pedidoList = await PedidoService.GetPedidosService(sqlDataSource);

            if (pedidoList.Count != 0)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Success!";
                response.Data = new JsonResult(pedidoList);
            }

            return response;
        }
    }
}
