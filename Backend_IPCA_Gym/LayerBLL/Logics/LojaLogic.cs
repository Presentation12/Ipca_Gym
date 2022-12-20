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
    public class LojaLogic
    {
        public static async Task<Response> GetLojaLogic(string sqlDataSource)
        {
            Response response = new Response();
            List<Loja> lojaList = await LojaService.GetAllService(sqlDataSource);

            if (lojaList.Count != 0)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Success!";
                response.Data = new JsonResult(lojaList);
            }

            return response;
        }
    }
}
